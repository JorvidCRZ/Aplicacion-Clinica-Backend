package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.response.NotificacionResponseDTO;
import com.proyectoClinica.model.*;
import com.proyectoClinica.repository.NotificacionRepository;
import com.proyectoClinica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificacionServiceImpl {
    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public void crearNotificacionSolicitudHorario(SolicitudHorario solicitud) {

        List<Usuario> administradores = usuarioRepository.findByRolNombre("Administrador");


        System.out.println("NOTIFICACIONES: Se encontraron " + administradores.size() + " administradores con rol 'Administrador'");

        for (Usuario admin : administradores) {
            Notificacion notificacion = Notificacion.builder()
                    .titulo("Nueva Solicitud de Horario")
                    .mensaje("El médico " + solicitud.getMedico().getPersona().getNombre1() + " " +
                            solicitud.getMedico().getPersona().getApellidoPaterno() +
                            " ha enviado una solicitud de horario para el " +
                            solicitud.getFecha() + " de " + solicitud.getHoraInicio() + " a " + solicitud.getHoraFin())
                    .tipo("SOLICITUD_HORARIO")
                    .usuarioDestino(admin)
                    .solicitudHorarioId(solicitud.getId())
                    .activa(true)
                    .leida(false)
                    .fechaCreacion(LocalDateTime.now())
                    .build();

            notificacionRepository.save(notificacion);
        }
    }

    public void crearNotificacionAprobacion(SolicitudHorario solicitud, Usuario admin) {
        Usuario usuarioMedico = solicitud.getMedico().getUsuario();

        Notificacion notificacion = Notificacion.builder()
                .titulo("Solicitud de Horario Aprobada")
                .mensaje("Tu solicitud de horario para el " + solicitud.getFecha() +
                        " ha sido aprobada. " + (solicitud.getComentariosAdmin() != null ?
                        "Comentarios: " + solicitud.getComentariosAdmin() : ""))
                .tipo("APROBACION_HORARIO")
                .usuarioDestino(usuarioMedico)
                .solicitudHorarioId(solicitud.getId())
                .activa(true)
                .leida(false)
                .fechaCreacion(LocalDateTime.now())
                .build();

        notificacionRepository.save(notificacion);
    }

    public void crearNotificacionRechazo(SolicitudHorario solicitud, Usuario admin) {
        Usuario usuarioMedico = solicitud.getMedico().getUsuario();

        Notificacion notificacion = Notificacion.builder()
                .titulo("Solicitud de Horario Rechazada")
                .mensaje("Tu solicitud de horario para el " + solicitud.getFecha() +
                        " ha sido rechazada. " + (solicitud.getComentariosAdmin() != null ?
                        "Motivo: " + solicitud.getComentariosAdmin() : ""))
                .tipo("RECHAZO_HORARIO")
                .usuarioDestino(usuarioMedico)
                .solicitudHorarioId(solicitud.getId())
                .activa(true)
                .leida(false)
                .fechaCreacion(LocalDateTime.now())
                .build();

        notificacionRepository.save(notificacion);
    }

    public List<NotificacionResponseDTO> obtenerNotificacionesUsuario(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return notificacionRepository.findByUsuarioDestinoAndActivaTrueOrderByFechaCreacionDesc(usuario)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<NotificacionResponseDTO> obtenerNotificacionesNoLeidas(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return notificacionRepository.findByUsuarioDestinoAndLeidaFalseAndActivaTrueOrderByFechaCreacionDesc(usuario)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public void marcarComoLeida(Long notificacionId, Integer usuarioId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        if (!notificacion.getUsuarioDestino().getIdUsuario().equals(usuarioId)) {
            throw new RuntimeException("No tienes permisos para esta notificación");
        }

        notificacion.setLeida(true);
        notificacion.setFechaLectura(LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }

    public Long contarNotificacionesNoLeidas(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return notificacionRepository.countByUsuarioDestinoAndLeidaFalseAndActivaTrue(usuario);
    }

    private NotificacionResponseDTO convertirADTO(Notificacion notificacion) {
        NotificacionResponseDTO dto = new NotificacionResponseDTO();
        dto.setId(notificacion.getId());
        dto.setTitulo(notificacion.getTitulo());
        dto.setMensaje(notificacion.getMensaje());
        dto.setTipo(notificacion.getTipo());
        dto.setLeida(notificacion.getLeida());
        dto.setFechaCreacion(notificacion.getFechaCreacion());
        dto.setSolicitudHorarioId(notificacion.getSolicitudHorarioId());
        return dto;
    }
}