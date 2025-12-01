package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.UsuarioEditRequestDTO;
import com.proyectoClinica.dto.request.UsuarioRequestDTO;
import com.proyectoClinica.dto.response.UsuarioResponseDTO;
import com.proyectoClinica.mapper.PersonaMapper;
import com.proyectoClinica.mapper.UsuarioMapper;
import com.proyectoClinica.model.Paciente;
import com.proyectoClinica.model.Persona;
import com.proyectoClinica.model.Usuario;
import com.proyectoClinica.repository.PacienteRepository;
import com.proyectoClinica.repository.PersonaRepository;
import com.proyectoClinica.repository.UsuarioRepository;
import com.proyectoClinica.repository.RecordatorioRepository;
import com.proyectoClinica.service.UsuarioService;
import lombok.RequiredArgsConstructor;
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
    private final PacienteRepository pacienteRepository;

    @Override
    public UsuarioResponseDTO crear(UsuarioRequestDTO requestDTO) {
        Persona persona = personaMapper.toEntity(requestDTO.getPersona());
        Persona personaGuardada = personaRepository.save(persona);

        Usuario usuario = usuarioMapper.toEntity(requestDTO);
        usuario.setPersona(personaGuardada);
        Usuario guardado = usuarioRepository.save(usuario);

        if (guardado.getRol() != null && guardado.getRol().getIdRol() == 3) {
            boolean existePaciente = pacienteRepository.existsByPersonaIdPersona(personaGuardada.getIdPersona());
            if (!existePaciente) {
                Paciente paciente = new Paciente();
                paciente.setPersona(personaGuardada);
                paciente.setUsuarioAgrego(guardado);
                pacienteRepository.save(paciente);
            }
        }

        // Backfill recordatorios pendientes
        try {
            if (personaGuardada.getIdPersona() != null) {
                Integer idPersona = personaGuardada.getIdPersona();
                var pendientes = recordatorioRepository
                        .findByCita_Paciente_Persona_IdPersonaAndDestinatarioCorreoIsNull(idPersona);
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

    @Override
    public UsuarioResponseDTO actualizarCorreo(UsuarioEditRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getIdUsuario()));

        usuario.setCorreo(dto.getCorreo());
        Usuario guardado = usuarioRepository.save(usuario);

        return usuarioMapper.toDTO(guardado);
    }

    @Override
    public UsuarioResponseDTO actualizar(Integer idUsuario, UsuarioRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + idUsuario));

        // Actualizar correo y demás campos si aplica
        usuario.setCorreo(requestDTO.getCorreo());

        if (requestDTO.getPersona() != null) {
            Persona persona = usuario.getPersona();
            personaMapper.updateFromDTO(requestDTO.getPersona(), persona); // Implementar método en mapper
            personaRepository.save(persona);
        }

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(guardado);
    }

}
