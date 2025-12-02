package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.AprobarSolicitudRequestDTO;
import com.proyectoClinica.dto.request.SolicitudHorarioRequestDTO;
import com.proyectoClinica.dto.response.SolicitudHorarioResponseDTO;
import com.proyectoClinica.model.*;
import com.proyectoClinica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SolicitudHorarioServiceImpl {
    private final SolicitudHorarioRepository solicitudHorarioRepository;
    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionServiceImpl notificacionService;
    private final HorarioDisponibleRepository horarioDisponibleRepository;

    public SolicitudHorarioResponseDTO crearSolicitud(SolicitudHorarioRequestDTO solicitudDTO, Integer usuarioId) {
        Medico medico = medicoRepository.findById(solicitudDTO.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        validarPermisosMedico(medico, usuarioId);
        validarSolicitudDuplicada(medico, solicitudDTO.getFecha(), solicitudDTO.getHoraInicio());

        SolicitudHorario solicitud = SolicitudHorario.builder()
                .medico(medico)
                .fecha(solicitudDTO.getFecha())
                .horaInicio(solicitudDTO.getHoraInicio())
                .horaFin(solicitudDTO.getHoraFin())
                .motivo(solicitudDTO.getMotivo())
                .estado("PENDIENTE")
                .fechaSolicitud(LocalDateTime.now())
                .build();

        SolicitudHorario solicitudGuardada = solicitudHorarioRepository.save(solicitud);

        try {
            notificacionService.crearNotificacionSolicitudHorario(solicitudGuardada);
        } catch (Exception e) {
            System.err.println("Error al enviar notificación: " + e.getMessage());
        }

        return convertirADTO(solicitudGuardada);
    }

    public SolicitudHorarioResponseDTO aprobarSolicitud(Long solicitudId, AprobarSolicitudRequestDTO requestDTO, Integer adminId) {
        SolicitudHorario solicitud = solicitudHorarioRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Usuario administrador no encontrado"));

        if (requestDTO.isAprobar()) {
            solicitud.setEstado("APROBADA");
            solicitud.setFechaAprobacion(LocalDateTime.now());
            solicitud.setAprobadoPor(admin);
            solicitud.setComentariosAdmin(requestDTO.getComentarios());

            HorarioDisponible horario = HorarioDisponible.builder()
                    .medico(solicitud.getMedico())
                    .fecha(solicitud.getFecha())
                    .horaInicio(solicitud.getHoraInicio())
                    .horaFin(solicitud.getHoraFin())
                    .disponible(true)
                    .build();
            horarioDisponibleRepository.save(horario);

            notificacionService.crearNotificacionAprobacion(solicitud, admin);

        } else {
            solicitud.setEstado("RECHAZADA");
            solicitud.setFechaAprobacion(LocalDateTime.now());
            solicitud.setAprobadoPor(admin);
            solicitud.setComentariosAdmin(requestDTO.getComentarios());

            notificacionService.crearNotificacionRechazo(solicitud, admin);
        }

        SolicitudHorario solicitudActualizada = solicitudHorarioRepository.save(solicitud);
        return convertirADTO(solicitudActualizada);
    }

    public SolicitudHorarioResponseDTO crearSolicitudComoAdmin(SolicitudHorarioRequestDTO solicitudDTO, Integer adminId) {
        Medico medico = medicoRepository.findById(solicitudDTO.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        String rolNombre = admin.getRol().getNombre();
        if (!rolNombre.equalsIgnoreCase("Administrador") && !rolNombre.equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Solo los administradores pueden usar este método");
        }

        validarSolicitudDuplicada(medico, solicitudDTO.getFecha(), solicitudDTO.getHoraInicio());

        SolicitudHorario solicitud = SolicitudHorario.builder()
                .medico(medico)
                .fecha(solicitudDTO.getFecha())
                .horaInicio(solicitudDTO.getHoraInicio())
                .horaFin(solicitudDTO.getHoraFin())
                .motivo("Creada por Admin: " + solicitudDTO.getMotivo())
                .estado("APROBADA")
                .fechaSolicitud(LocalDateTime.now())
                .fechaAprobacion(LocalDateTime.now())
                .aprobadoPor(admin)
                .comentariosAdmin("Aprobado automáticamente")
                .build();

        SolicitudHorario solicitudGuardada = solicitudHorarioRepository.save(solicitud);

        HorarioDisponible horario = HorarioDisponible.builder()
                .medico(medico)
                .fecha(solicitud.getFecha())
                .horaInicio(solicitud.getHoraInicio())
                .horaFin(solicitud.getHoraFin())
                .disponible(true)
                .build();
        horarioDisponibleRepository.save(horario);

        notificacionService.crearNotificacionAprobacion(solicitudGuardada, admin);

        return convertirADTO(solicitudGuardada);
    }

    public List<SolicitudHorarioResponseDTO> obtenerSolicitudesPendientes() {
        return solicitudHorarioRepository.findByEstado("PENDIENTE")
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<SolicitudHorarioResponseDTO> obtenerSolicitudesPorUsuario(Integer usuarioId) {
        Medico medico = medicoRepository.findByUsuarioIdUsuario(usuarioId)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado para este usuario"));

        return solicitudHorarioRepository.findByMedicoOrderByFechaSolicitudDesc(medico)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private void validarPermisosMedico(Medico medico, Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String rol = usuario.getRol().getNombre();
        if (rol.equalsIgnoreCase("Administrador") || rol.equalsIgnoreCase("ADMIN")) {
            return;
        }

        if (!medico.getUsuario().getIdUsuario().equals(usuarioId)) {
            throw new RuntimeException("No tienes permisos para gestionar horarios de este médico");
        }
    }

    private void validarSolicitudDuplicada(Medico medico, LocalDate fecha, LocalTime horaInicio) {
        // CORRECCIÓN AQUÍ: Pasamos el objeto 'medico' completo, no 'medico.getIdMedico()'
        List<SolicitudHorario> solicitudesExistentes = solicitudHorarioRepository.findByMedicoAndFecha(medico, fecha);

        boolean existeDuplicado = solicitudesExistentes.stream()
                .anyMatch(s -> s.getHoraInicio().equals(horaInicio) && !s.getEstado().equals("RECHAZADA"));

        if (existeDuplicado) {
            throw new RuntimeException("Ya existe una solicitud para esta fecha y hora");
        }
    }

    private SolicitudHorarioResponseDTO convertirADTO(SolicitudHorario solicitud) {
        SolicitudHorarioResponseDTO dto = new SolicitudHorarioResponseDTO();
        dto.setId(solicitud.getId());
        dto.setMedicoId(solicitud.getMedico().getIdMedico());
        dto.setMedicoNombre(solicitud.getMedico().getPersona().getNombre1() + " " +
                solicitud.getMedico().getPersona().getApellidoPaterno());
        dto.setFecha(solicitud.getFecha());
        dto.setHoraInicio(solicitud.getHoraInicio());
        dto.setHoraFin(solicitud.getHoraFin());
        dto.setEstado(solicitud.getEstado());
        dto.setMotivo(solicitud.getMotivo());
        dto.setFechaSolicitud(solicitud.getFechaSolicitud());
        dto.setFechaAprobacion(solicitud.getFechaAprobacion());
        dto.setComentariosAdmin(solicitud.getComentariosAdmin());
        return dto;
    }
}