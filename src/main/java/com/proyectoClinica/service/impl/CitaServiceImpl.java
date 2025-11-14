package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.CitaRequestDTO;
import com.proyectoClinica.dto.response.CitaMedicoViewDTO;
import com.proyectoClinica.dto.response.CitaResponseDTO;
import com.proyectoClinica.mapper.CitaMapper;
import com.proyectoClinica.model.DetalleCita;
import com.proyectoClinica.model.DisponibilidadMedico;
import com.proyectoClinica.model.Paciente;
import com.proyectoClinica.repository.DetalleCitaRepository;
import com.proyectoClinica.repository.DisponibilidadMedicoRepository;
import com.proyectoClinica.repository.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.proyectoClinica.model.Cita;
import com.proyectoClinica.repository.CitaRepository;
import com.proyectoClinica.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final PacienteRepository pacienteRepository;
    private final DetalleCitaRepository detalleCitaRepository;
    private final DisponibilidadMedicoRepository disponibilidadMedicoRepository;

    @Override
    public CitaResponseDTO crear(CitaRequestDTO requestDTO) {
        // Validaciones y carga de entidades relacionadas
        Paciente paciente = pacienteRepository.findById(requestDTO.getIdPaciente())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));

        DetalleCita detalleCita = detalleCitaRepository.findById(requestDTO.getIdDetalleCita())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de cita no encontrado"));

        DisponibilidadMedico disponibilidad = disponibilidadMedicoRepository.findById(requestDTO.getIdDisponibilidad())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disponibilidad no encontrada"));

        // Disponibilidad debe estar vigente y el día activo
        if (disponibilidad.getVigencia() != null && !disponibilidad.getVigencia()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La disponibilidad no está vigente");
        }
        if (disponibilidad.getDiaActivo() != null && !disponibilidad.getDiaActivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El día del médico está marcado como inactivo");
        }

        // Validar que la disponibilidad corresponda al mismo médico del detalle (medicoEspecialidad)
        Integer medicoFromDetalle = null;
        if (detalleCita.getMedicoEspecialidad() != null && detalleCita.getMedicoEspecialidad().getMedico() != null) {
            medicoFromDetalle = detalleCita.getMedicoEspecialidad().getMedico().getIdMedico();
        }
        Integer medicoFromDisponibilidad = null;
        if (disponibilidad.getMedico() != null) {
            medicoFromDisponibilidad = disponibilidad.getMedico().getIdMedico();
        }
        if (medicoFromDetalle != null && medicoFromDisponibilidad != null && !medicoFromDetalle.equals(medicoFromDisponibilidad)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La disponibilidad seleccionada no pertenece al médico de la especialidad/turno elegido");
        }

        // Validar que la fecha coincida con el día de la disponibilidad (comparar nombres de día en español)
        String diaFecha = nombreDiaEnEspannol(requestDTO.getFechaCita());
        if (disponibilidad.getDiaSemana() != null && !disponibilidad.getDiaSemana().equalsIgnoreCase(diaFecha)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha seleccionada no corresponde al día de la disponibilidad");
        }

        // Validar hora dentro del rango
        if (requestDTO.getHoraCita().isBefore(disponibilidad.getHoraInicio()) || requestDTO.getHoraCita().isAfter(disponibilidad.getHoraFin()) || requestDTO.getHoraCita().equals(disponibilidad.getHoraFin())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La hora seleccionada está fuera del rango de la disponibilidad");
        }

        // Si la disponibilidad tiene duracion_minutos, validar alineamiento
        if (disponibilidad.getDuracionMinutos() != null && disponibilidad.getDuracionMinutos() > 0) {
            long minutosDesdeInicio = java.time.Duration.between(disponibilidad.getHoraInicio(), requestDTO.getHoraCita()).toMinutes();
            if (minutosDesdeInicio < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La hora seleccionada es anterior al inicio de la disponibilidad");
            }

            // permitir sub-intervalos (por ejemplo 30 minutos dentro de duracion 60)
            int duracion = disponibilidad.getDuracionMinutos();
            int allowedInterval = gcd(duracion, 30); // permite 30,15,10,5 según duracion
            if (allowedInterval <= 0) allowedInterval = duracion;

            if (minutosDesdeInicio % allowedInterval != 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La hora seleccionada no está alineada con los intervalos permitidos (cada " + allowedInterval + " minutos)");
            }
        }

        // Validar que no exista otra cita en la misma disponibilidad/fecha/hora
        boolean existe = citaRepository.existsByDisponibilidad_IdDisponibilidadAndFechaCitaAndHoraCita(
                disponibilidad.getIdDisponibilidad(), requestDTO.getFechaCita(), requestDTO.getHoraCita());
        if (existe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una cita reservada en ese horario");
        }

        // Construir entidad y guardar
        Cita cita = Cita.builder()
                .paciente(paciente)
                .detalleCita(detalleCita)
                .disponibilidad(disponibilidad)
                .fechaCita(requestDTO.getFechaCita())
                .horaCita(requestDTO.getHoraCita())
                .estado(requestDTO.getEstado())
                .motivoConsulta(requestDTO.getMotivoConsulta())
                .build();

        Cita guardado = citaRepository.save(cita);
        return citaMapper.toDTO(guardado);
    }

    @Override
    public CitaResponseDTO obtenerPorId(Integer id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        return citaMapper.toDTO(cita);
    }

    @Override
    public List<CitaResponseDTO> listar() {
        return citaMapper.toDTOList(citaRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        citaRepository.deleteById(id);
    }

    @Override
    public List<CitaResponseDTO> listarCitasDeHoy() {
        LocalDate hoy=LocalDate.now();
        List<Cita> citasDeHoy=citaRepository.findByFechaCita(hoy);
        return citaMapper.toDTOList(citasDeHoy);
    }

    // Euclid's algorithm: helper to compute allowed sub-intervals
    private int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        if (a == 0) return b;
        if (b == 0) return a;
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    private String nombreDiaEnEspannol(java.time.LocalDate fecha) {
        java.time.DayOfWeek dow = fecha.getDayOfWeek();
        switch (dow) {
            case MONDAY: return "Lunes";
            case TUESDAY: return "Martes";
            case WEDNESDAY: return "Miércoles";
            case THURSDAY: return "Jueves";
            case FRIDAY: return "Viernes";
            case SATURDAY: return "Sábado";
            case SUNDAY: return "Domingo";
            default: return dow.name();
        }
    }

    /*numero de citas de medico , numero de citas del medico en este mes*/
    @Override
    public long contarCitasPorMedico(Integer idMedico) {
        return citaRepository.contarCitasPorMedico(idMedico);
    }

    @Override
    public long contarCitasDelMesActualPorMedico(Long idMedico) {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate finMes = inicioMes.plusMonths(1).minusDays(1);
        return citaRepository.countByMedicoIdAndFechaBetween(idMedico, inicioMes, finMes);
    }



    /*Citas por dASHBOARD mEDICO*/

    @Override
    public List<CitaMedicoViewDTO> listarCitasDashboardPorMedico(Integer idMedico) {
        return citaRepository.listarCitasDashboardPorMedico(idMedico)
                .stream()
                .map(row -> CitaMedicoViewDTO.builder()
                        .fecha(row.get("fecha") != null ? row.get("fecha").toString() : null)
                        .hora(row.get("hora") != null ? row.get("hora").toString() : null)
                        .paciente((String) row.get("paciente"))
                        .documento((String) row.get("documento"))
                        .telefono((String) row.get("telefono"))
                        .tipoConsulta((String) row.get("motivo_consulta"))
                        .estado((String) row.get("estado"))
                        .build())
                .toList();
    }



}
