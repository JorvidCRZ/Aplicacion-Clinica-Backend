package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.ReporteRequestDTO;
import com.proyectoClinica.dto.response.ReporteResponseDTO;
import com.proyectoClinica.mapper.ReporteMapper;
import com.proyectoClinica.model.Medico;
import com.proyectoClinica.model.Paciente;
import com.proyectoClinica.model.Reporte;
import com.proyectoClinica.repository.MedicoRepository;
import com.proyectoClinica.repository.PacienteRepository;
import com.proyectoClinica.repository.ReporteRepository;
import com.proyectoClinica.service.ReporteService;
import com.proyectoClinica.service.export.ReporteExcelExporter;
import com.proyectoClinica.service.export.ReporteMedicoExcelExporter;
import com.proyectoClinica.service.export.ReporteMedicoPDFExporter;
import com.proyectoClinica.service.export.ReportePDFExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
