package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.UsuarioRequestDTO;
import com.proyectoClinica.dto.response.UsuarioResponseDTO;
import com.proyectoClinica.mapper.PersonaMapper;
import com.proyectoClinica.mapper.UsuarioMapper;
import com.proyectoClinica.model.Persona;
import com.proyectoClinica.model.Usuario;
import com.proyectoClinica.repository.PersonaRepository;
import com.proyectoClinica.repository.UsuarioRepository;
import com.proyectoClinica.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import com.proyectoClinica.repository.RecordatorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final UsuarioMapper usuarioMapper;
    private final PersonaMapper personaMapper;
    private final RecordatorioRepository recordatorioRepository;
    private final RecordatorioSender recordatorioSender;

    @Override
    public UsuarioResponseDTO crear(UsuarioRequestDTO requestDTO) {
        Persona persona = personaMapper.toEntity(requestDTO.getPersona());
        Persona personaGuardada = personaRepository.save(persona);

        Usuario usuario = usuarioMapper.toEntity(requestDTO);
        usuario.setPersona(personaGuardada);
        Usuario guardado = usuarioRepository.save(usuario);
        // Tras crear el usuario, backfill recordatorios pendientes que no tienen destinatario
        try {
            if (personaGuardada.getIdPersona() != null) {
                Integer idPersona = personaGuardada.getIdPersona();
                var pendientes = recordatorioRepository.findByCita_Paciente_Persona_IdPersonaAndDestinatarioCorreoIsNull(idPersona);
                for (var r : pendientes) {
                    r.setDestinatarioCorreo(guardado.getCorreo());
                    recordatorioRepository.save(r);
                    try {
                        if (r.getFechaEnvio() != null && !r.getFechaEnvio().isAfter(java.time.LocalDateTime.now())) {
                            recordatorioSender.enviarAsync(r.getIdRecordatorio());
                        }
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception ex) {
            org.slf4j.LoggerFactory.getLogger(UsuarioServiceImpl.class)
                    .warn("Error al backfill recordatorios tras crear usuario: {}", ex.getMessage());
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
