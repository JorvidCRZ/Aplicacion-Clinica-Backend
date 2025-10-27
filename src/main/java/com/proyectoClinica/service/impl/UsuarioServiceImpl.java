package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.UsuarioRequestDTO;
import com.proyectoClinica.dto.response.UsuarioResponseDTO;
import com.proyectoClinica.mapper.UsuarioMapper;
import com.proyectoClinica.model.Usuario;
import com.proyectoClinica.repository.UsuarioRepository;
import com.proyectoClinica.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import com.proyectoClinica.repository.RecordatorioRepository;
import com.proyectoClinica.service.impl.RecordatorioSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final RecordatorioRepository recordatorioRepository;
    private final RecordatorioSender recordatorioSender;

    @Override
    public UsuarioResponseDTO crear(UsuarioRequestDTO requestDTO) {
        Usuario usuario = usuarioMapper.toEntity(requestDTO);
        Usuario guardado = usuarioRepository.save(usuario);
        // Tras crear el usuario, backfill recordatorios pendientes que no tienen destinatario
        try {
            if (guardado.getPersona() != null && guardado.getPersona().getIdPersona() != null) {
                Integer idPersona = guardado.getPersona().getIdPersona();
                java.util.List<com.proyectoClinica.model.Recordatorio> pendientes = recordatorioRepository
                        .findByCita_Paciente_Persona_IdPersonaAndDestinatarioCorreoIsNull(idPersona);
                for (com.proyectoClinica.model.Recordatorio r : pendientes) {
                    r.setDestinatarioCorreo(guardado.getCorreo());
                    recordatorioRepository.save(r);
                    // si la fecha de envío ya llegó, solicitar envío async
                    try {
                        if (r.getFechaEnvio() != null && !r.getFechaEnvio().isAfter(java.time.LocalDateTime.now())) {
                            recordatorioSender.enviarAsync(r.getIdRecordatorio());
                        }
                    } catch (Exception ex) {
                        // log and continue
                        org.slf4j.LoggerFactory.getLogger(UsuarioServiceImpl.class).warn("No se pudo solicitar envio async para recordatorio {}: {}", r.getIdRecordatorio(), ex.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            org.slf4j.LoggerFactory.getLogger(UsuarioServiceImpl.class).warn("Error al backfill recordatorios tras crear usuario: {}", ex.getMessage());
        }

        return usuarioMapper.toDTO(guardado);
    }

    @Override
    public UsuarioResponseDTO obtenerPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioMapper.toDTO(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> listar() {
        return usuarioMapper.toDTOList(usuarioRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
