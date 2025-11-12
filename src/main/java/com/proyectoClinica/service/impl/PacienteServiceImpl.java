package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.PacienteRequestDTO;
import com.proyectoClinica.dto.response.PacienteDashboardDTO;
import com.proyectoClinica.dto.response.PacienteListadoResponseDTO;
import com.proyectoClinica.dto.response.PacienteResponseDTO;
import com.proyectoClinica.mapper.PacienteMapper;
import com.proyectoClinica.model.Paciente;
import com.proyectoClinica.model.Persona;
import com.proyectoClinica.model.Usuario;
import com.proyectoClinica.repository.PacienteRepository;
import com.proyectoClinica.repository.PersonaRepository;
import com.proyectoClinica.repository.UsuarioRepository;
import com.proyectoClinica.service.PacienteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;
    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public PacienteResponseDTO crear(PacienteRequestDTO requestDTO) {
        Integer idPersona = requestDTO.getIdPersona();
        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + idPersona));

        Paciente paciente = pacienteMapper.toEntity(requestDTO);
        paciente.setPersona(persona);

        Paciente guardado = pacienteRepository.save(paciente);
        return pacienteMapper.toDTO(guardado);
    }

    @Override
    public PacienteResponseDTO obtenerPorId(Integer id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        return pacienteMapper.toDTO(paciente);
    }

    @Override
    public List<PacienteResponseDTO> listar() {
        return pacienteMapper.toDTOList(pacienteRepository.findAll());
    }

    @Override
    @Transactional
    public PacienteResponseDTO actualizar(Integer id, PacienteRequestDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        // Actualización de Persona
        Persona persona = paciente.getPersona();
        if (dto.getPersona() != null) {
            persona.setNombre1(dto.getPersona().getNombre1());
            persona.setNombre2(dto.getPersona().getNombre2());
            persona.setApellidoPaterno(dto.getPersona().getApellidoPaterno());
            persona.setApellidoMaterno(dto.getPersona().getApellidoMaterno());
            persona.setGenero(dto.getPersona().getGenero());
            persona.setDireccion(dto.getPersona().getDireccion());
            persona.setTelefono(dto.getPersona().getTelefono());
            persona.setFechaNacimiento(dto.getPersona().getFechaNacimiento());
            personaRepository.save(persona);
        }

        if (dto.getUsuarioAgrego() != null && dto.getUsuarioAgrego().getCorreo() != null) {
            Usuario usuario = paciente.getUsuarioAgrego();
            if (usuario == null) {
                usuario = usuarioRepository.findByPersona_IdPersona(persona.getIdPersona())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "No se encontró usuario asociado al paciente"));
            }
            usuario.setCorreo(dto.getUsuarioAgrego().getCorreo());
            usuarioRepository.save(usuario);
        }

        paciente.setTipoSangre(dto.getTipoSangre());
        paciente.setPeso(dto.getPeso());
        paciente.setAltura(dto.getAltura());
        paciente.setContactoEmergenciaNombre(dto.getContactoEmergenciaNombre());
        paciente.setContactoEmergenciaRelacion(dto.getContactoEmergenciaRelacion());
        paciente.setContactoEmergenciaTelefono(dto.getContactoEmergenciaTelefono());

        Paciente actualizado = pacienteRepository.save(paciente);
        return pacienteMapper.toDTO(actualizado);
    }

    @Override
    public PacienteResponseDTO obtenerPorUsuarioId(Integer idUsuario) {
        Paciente paciente = pacienteRepository.findByPersonaIdPersona(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado para el usuario con ID: " + idUsuario));
        return pacienteMapper.toDTO(paciente);
    }

    @Override
    public List<PacienteListadoResponseDTO> listarPacientesDetalle() {
        return pacienteRepository.listarPacientesDetalle()
                .stream()
                .map(row -> PacienteListadoResponseDTO.builder()
                        .id(((Number) row.get("id")).longValue())
                        .nombreCompleto((String) row.get("nombreCompleto"))
                        .email((String) row.get("email"))
                        .telefono((String) row.get("telefono"))
                        .tipoDocumento((String) row.get("tipoDocumento"))
                        .numeroDocumento((String) row.get("numeroDocumento"))
                        .fechaNacimiento(String.valueOf(row.get("fechaNacimiento")))
                        .genero((String) row.get("genero"))
                        .build())
                .toList();
    }
    @Override
    public void eliminar(Integer id) {
        pacienteRepository.deleteById(id);
    }


    /*mETODO DE PACIENTE TABLA DASHBOARD(MEDICO)*/

    @Override
    public List<PacienteDashboardDTO> listarDashboardPorMedico(Integer idMedico) {
        return pacienteRepository.listarPacientesDashboardPorMedico(idMedico)
                .stream()
                .map(row -> PacienteDashboardDTO.builder()
                        .idPaciente(((Number) row.get("idpaciente")).longValue())
                        .nombreCompleto((String) row.get("nombrecompleto"))
                        .edad(((Number) row.get("edad")).intValue())
                        .genero((String) row.get("genero"))

                        // 👇 Mapeo correcto según tu vista SQL
                        .contactoEmail((String) row.get("contacto_email"))
                        .contactoTelefonos((String) row.get("contacto_telefonos"))

                        .ultimaCita(row.get("ultimacita") != null ? row.get("ultimacita").toString() : null)
                        .diagnostico((String) row.get("diagnostico"))
                        .build())
                .toList();
    }




}
