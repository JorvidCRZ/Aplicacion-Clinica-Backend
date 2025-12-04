package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.ReporteRequestDTO;
import com.proyectoClinica.dto.response.*;
import com.proyectoClinica.mapper.ReporteMapper;
import com.proyectoClinica.model.*;
import com.proyectoClinica.repository.*;
import com.proyectoClinica.service.ReporteService;
import com.proyectoClinica.service.export.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    private final ReporteMapper reporteMapper;

    private final ReportePDFExporter pdfExporter;
    private final ReporteExcelExporter excelExporter;
    private final ReporteMedicoPDFExporter medicoPDFExporter;
    private final ReporteMedicoExcelExporter medicoExcelExporter;
    private final ReporteEstadisticaPacientePDFExporter reporteEstadisticaPacientePDFExporter;
    private final ReporteUsuarioPDFExporter reporteUsuarioPDFExporter;

    private final CitaRepository citaRepository;
    private final EstadisticaPacienteService estadisticaPacienteService;
    private final UsuarioRepository usuarioRepository;
    //
    private final ReporteGeneralPDFExporter reporteGeneralPDFExporter;
    private final ReporteRendimientoDoctorPDFExporter reporteRendimientoDoctorPDFExporter;
    private final ReporteCitasPDFExporter reporteCitasPDFExporter;

    @Override
    public ReporteResponseDTO crear(ReporteRequestDTO requestDTO) {
        Reporte reporte = reporteMapper.toEntity(requestDTO);
        Reporte guardado = reporteRepository.save(reporte);
        return reporteMapper.toDTO(guardado);
    }

    @Override
    public ReporteResponseDTO obtenerPorId(Integer id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        return reporteMapper.toDTO(reporte);
    }

    @Override
    public List<ReporteResponseDTO> listar() {
        return reporteMapper.toDTOList(reporteRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        reporteRepository.deleteById(id);
    }

    @Override
    public byte[] generarReportePacientesPDF() {
        List<Paciente> pacientes = pacienteRepository.findAll();
        return pdfExporter.exportar(pacientes);
    }

    @Override
    public byte[] generarReportePacientesExcel() {
        List<Paciente> pacientes = pacienteRepository.findAll();
        return excelExporter.exportar(pacientes);
    }

    @Override
    public byte[] generarReporteMedicoPDF() {
        List<Medico> medicos = medicoRepository.findAll();
        return medicoPDFExporter.exportar(medicos);
    }

    @Override
    public byte[] generarReporteMedicoExcel() {
        List<Medico> medicos = medicoRepository.findAll();
        return medicoExcelExporter.exportar(medicos);

    }

    // MODIFICACIÓN
    @Override
    public byte[] generarReporteGeneralPDF(LocalDate inicio, LocalDate fin, int topN) {
        ReporteGeneralDTO reporte = generarReporteGeneral(inicio, fin, topN);
        return reporteGeneralPDFExporter.exportar(reporte);
    }

    public ReporteGeneralDTO generarReporteGeneral(LocalDate inicio, LocalDate fin, int topN) {
        if (inicio == null) inicio = LocalDate.now().withDayOfMonth(1);
        if (fin == null) fin = LocalDate.now();

        // 1) conteo por estado
        List<Map<String, Object>> rows = citaRepository.contarCitasPorEstadoEntreFechas(inicio, fin);

        long confirmadas = 0L, pendientes = 0L, canceladas = 0L;
        long total = 0L;

        for (Map<String, Object> r : rows) {
            String estado = r.get("estado") != null ? r.get("estado").toString().trim() : "";
            long cantidad = r.get("cantidad") != null ? ((Number) r.get("cantidad")).longValue() : 0L;
            total += cantidad;

            // mapear tolerante a mayúsculas/minúsculas y plurales
            String estLower = estado.toLowerCase(Locale.ROOT);
            if (estLower.contains("confirm")) { // "Confirmada", "Confirmadas"
                confirmadas = cantidad;
            } else if (estLower.contains("pend")) { // "Pendiente", "Pendientes"
                pendientes = cantidad;
            } else if (estLower.contains("cancel")) { // "Cancelada", "Canceladas"
                canceladas = cantidad;
            } else {
                // si tu sistema usa otros estados puedes mapearlos aquí
            }
        }

        double pctConfirmadas = total > 0 ? (confirmadas * 100.0 / total) : 0.0;
        double pctPendientes = total > 0 ? (pendientes * 100.0 / total) : 0.0;
        double pctCanceladas = total > 0 ? (canceladas * 100.0 / total) : 0.0;

        EstadoCitaDTO estadoDto = EstadoCitaDTO.builder()
                .confirmadas(confirmadas)
                .pendientes(pendientes)
                .canceladas(canceladas)
                .porcentajeConfirmadas(Math.round(pctConfirmadas * 100.0) / 100.0)
                .porcentajePendientes(Math.round(pctPendientes * 100.0) / 100.0)
                .porcentajeCanceladas(Math.round(pctCanceladas * 100.0) / 100.0)
                .build();

        // 2) top doctores
        List<Map<String, Object>> docRows = citaRepository.topDoctoresEntreFechas(inicio, fin, topN);
        List<TopDoctorDTO> topDoctores = docRows.stream().map(m -> {
            String nombre = m.get("nombre") != null ? m.get("nombre").toString().trim() : "—";
            String especialidad = m.get("especialidad") != null ? m.get("especialidad").toString() : "—";
            long t = m.get("total") != null ? ((Number) m.get("total")).longValue() : 0L;
            return TopDoctorDTO.builder()
                    .nombreCompleto(nombre)
                    .especialidad(especialidad)
                    .totalCitas(t)
                    .build();
        }).collect(Collectors.toList());

        return ReporteGeneralDTO.builder()
                .fechaInicio(inicio)
                .fechaFin(fin)
                .estadoCitas(estadoDto)
                .topDoctores(topDoctores)
                .totalCitas(total)
                .build();
    }

    // -------

    @Override
    public byte[] generarReporteRendimientoDoctoresPDF() {
        List<ReporteRendimientoDoctorDTO> data = citaRepository.reporteRendimientoDoctores();
        return reporteRendimientoDoctorPDFExporter.exportar(data);
    }

    public List<ReporteRendimientoDoctorDTO> reporteRendimientoDoctores() {

        List<Medico> medicos = medicoRepository.findAll();

        return medicos.stream().map(m -> {
            long total = citaRepository.contarTotalCitas(m.getIdMedico());
            return ReporteRendimientoDoctorDTO.builder()
                    .nombreDoctor(m.getPersona().getNombre1() + " " +
                            m.getPersona().getApellidoPaterno() + " " +
                            m.getPersona().getApellidoMaterno())
                    .especialidad(
                            m.getEspecialidades().isEmpty() ?
                                    "Sin especialidad" :
                                    m.getEspecialidades().get(0).getEspecialidad().getNombre()
                    )
                    .totalCitas(total)
                    .citasCompletadas(citaRepository.contarCompletadas(m.getIdMedico()))
                    .citasCanceladas(citaRepository.contarCanceladas(m.getIdMedico()))
                    .citasPendientes(citaRepository.contarPendientes(m.getIdMedico()))
                    .ingresosTotales(
                            Optional.ofNullable(citaRepository.calcularIngresos(m.getIdMedico()))
                                    .orElse(BigDecimal.ZERO)
                    )
                    .build();
        }).toList();
    }
// ------------------

    public byte[] generarReporteEstadisticaPacientePDF() {
        List<EstadisticaPacienteDTO> data = estadisticaPacienteService.obtenerEstadisticasPacientes();
        return reporteEstadisticaPacientePDFExporter.exportar(data);
    }

    //------------
    @Override
    @Transactional // IMPORTANTE: Para que cargue Persona y Rol (Lazy Loading)
    public byte[] generarReporteUsuariosPDF() {
        // Usamos el método con JOIN FETCH que definimos en la respuesta anterior
        List<Usuario> usuarios = usuarioRepository.findAllWithPersonaAndRol();

        // Llamamos al exporter que usa iText para generar el PDF
        return reporteUsuarioPDFExporter.exportar(usuarios);
    }

    //------------

    @Override
    @Transactional
    public byte[] generarReporteCitasPDF() {
        // Usamos el método con JOIN FETCH
        List<Cita> citas = citaRepository.findAllForReport();

        // Generamos el PDF
        return reporteCitasPDFExporter.exportar(citas);
    }

    //----------------
    // ReporteServiceImpl.java

    // ... Inyecciones Existentes ...
    private final PagoDetalleRepository pagoDetalleRepository; // <--- Agregar
    private final ReporteFacturasPDFExporter reporteFacturasPDFExporter; // <--- Agregar
// ...

    @Override
    @Transactional
    public byte[] generarReporteFacturasPDF() {
        // Usamos el método con JOIN FETCH
        List<PagoDetalle> detalles = pagoDetalleRepository.findAllForReport();

        // Generamos el PDF
        return reporteFacturasPDFExporter.exportar(detalles);
    }
}
