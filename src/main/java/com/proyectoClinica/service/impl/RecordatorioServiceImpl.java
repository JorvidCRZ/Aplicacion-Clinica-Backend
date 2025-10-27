package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.RecordatorioRequestDTO;
import com.proyectoClinica.dto.response.RecordatorioResponseDTO;
import com.proyectoClinica.mapper.RecordatorioMapper;
import com.proyectoClinica.model.Recordatorio;
import com.proyectoClinica.repository.RecordatorioRepository;
import com.proyectoClinica.service.RecordatorioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordatorioServiceImpl implements RecordatorioService {

    private final RecordatorioRepository recordatorioRepository;
    private final RecordatorioMapper recordatorioMapper;
    private final com.proyectoClinica.repository.CitaRepository citaRepository;
    private final com.proyectoClinica.repository.UsuarioRepository usuarioRepository;
    private final com.proyectoClinica.service.impl.RecordatorioSender recordatorioSender;


    private static final Logger log = LoggerFactory.getLogger(RecordatorioServiceImpl.class);

    @Override
    public RecordatorioResponseDTO crear(RecordatorioRequestDTO requestDTO) {
        // Buscar la cita y setear la relación explícitamente
        com.proyectoClinica.model.Cita cita = citaRepository.findById(requestDTO.getIdCita())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Recordatorio recordatorio = recordatorioMapper.toEntity(requestDTO);
        recordatorio.setCita(cita);

        // Determinar destinatario: si viene en el DTO usamos, sino intentamos derivarlo desde la cita -> paciente -> persona -> usuario
        String destinatario = requestDTO.getDestinatarioCorreo();
        if (destinatario == null || destinatario.isBlank()) {
            Integer idPersona = null;
            if (cita.getPaciente() != null && cita.getPaciente().getPersona() != null) {
                idPersona = cita.getPaciente().getPersona().getIdPersona();
            }
            if (idPersona != null) {
                Optional<com.proyectoClinica.model.Usuario> usuarioOpt = usuarioRepository.findByPersona_IdPersona(idPersona);
                if (usuarioOpt.isPresent()) {
                    destinatario = usuarioOpt.get().getCorreo();
                }
            }
        }

        // Trim destinatario to avoid trailing spaces from DB or input
        if (destinatario != null) destinatario = destinatario.trim();
        recordatorio.setDestinatarioCorreo(destinatario);

        // Guardar inicial con estado proporcionado (o PENDIENTE si no viene)
        if (recordatorio.getEstado() == null || recordatorio.getEstado().isBlank()) {
            recordatorio.setEstado("PENDIENTE");
        }

        // Si fechaEnvio no viene en el DTO, asignamos ahora para cumplir la restricción NOT NULL
        if (recordatorio.getFechaEnvio() == null) {
            recordatorio.setFechaEnvio(java.time.LocalDateTime.now());
        }

        Recordatorio guardado = recordatorioRepository.save(recordatorio);

        // Si fechaEnvio es null o ya llegó (<= now) intentamos enviar inmediatamente (async).
        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
        if (guardado.getFechaEnvio() == null || !guardado.getFechaEnvio().isAfter(ahora)) {
            // enviar solo si tenemos destinatario
            if (destinatario != null && !destinatario.isBlank()) {
                try {
                    recordatorioSender.enviarAsync(guardado.getIdRecordatorio());
                } catch (Exception ex) {
                    log.error("Error solicitando envio async para recordatorio {}: {}", guardado.getIdRecordatorio(), ex.getMessage());
                }
            }
        }

        return recordatorioMapper.toDTO(guardado);
    }

    // No enviar sincronamente aquí: delegamos a RecordatorioSender (async)

    @Override
    public void enviarPendientes() {
        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
        java.util.List<Recordatorio> pendientes = recordatorioRepository.findAll().stream()
                .filter(r -> "PENDIENTE".equalsIgnoreCase(r.getEstado()))
                .filter(r -> r.getFechaEnvio() != null && !r.getFechaEnvio().isAfter(ahora))
                .toList();

    for (Recordatorio r : pendientes) {
            // intentar determinar destinatario si no está
            String destinatario = r.getDestinatarioCorreo();
            if ((destinatario == null || destinatario.isBlank()) && r.getCita() != null
                    && r.getCita().getPaciente() != null && r.getCita().getPaciente().getPersona() != null) {
                Integer idPersona = r.getCita().getPaciente().getPersona().getIdPersona();
                usuarioRepository.findByPersona_IdPersona(idPersona).ifPresent(u -> {}
                );
                java.util.Optional<com.proyectoClinica.model.Usuario> usuarioOpt = usuarioRepository.findByPersona_IdPersona(idPersona);
                if (usuarioOpt.isPresent()) {
                    destinatario = usuarioOpt.get().getCorreo();
                    r.setDestinatarioCorreo(destinatario);
                }
            }

            if (destinatario != null && !destinatario.isBlank()) {
                try {
                    recordatorioSender.enviarAsync(r.getIdRecordatorio());
                } catch (Exception ex) {
                    log.error("Error solicitando envio async para recordatorio {}: {}", r.getIdRecordatorio(), ex.getMessage());
                }
            }
        }
    }

    @Override
    public RecordatorioResponseDTO obtenerPorId(Integer id) {
        Recordatorio recordatorio = recordatorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recordatorio no encontrado"));
        return recordatorioMapper.toDTO(recordatorio);
    }

    @Override
    public List<RecordatorioResponseDTO> listar() {
        return recordatorioMapper.toDTOList(recordatorioRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        recordatorioRepository.deleteById(id);
    }
}
