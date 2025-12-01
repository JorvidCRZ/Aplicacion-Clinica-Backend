package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.ActualizarPerfilMedicoRequestDTO;
import com.proyectoClinica.dto.request.MedicoRequestDTO;
import com.proyectoClinica.dto.response.MedicoListadoResponseDTO;
import com.proyectoClinica.dto.response.MedicoResponseDTO;
import com.proyectoClinica.dto.response.PerfilMedicoDTO;
import com.proyectoClinica.mapper.MedicoMapper;
import com.proyectoClinica.model.Persona;
import com.proyectoClinica.model.Usuario;
import com.proyectoClinica.model.Medico;
import com.proyectoClinica.repository.PersonaRepository;
import com.proyectoClinica.repository.MedicoRepository;
import com.proyectoClinica.repository.UsuarioRepository;
import com.proyectoClinica.service.MedicoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;
    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;

    // =====================================================
    // CREAR MÉDICO
    // =====================================================
    @Override
    public MedicoResponseDTO crear(MedicoRequestDTO requestDTO) {

        Persona persona = personaRepository.findById(requestDTO.getIdPersona())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Persona no encontrada con id: " + requestDTO.getIdPersona()));

        Medico medico = medicoMapper.toEntity(requestDTO);
        medico.setPersona(persona);

        Medico guardado = medicoRepository.save(medico);
        return medicoMapper.toDTO(guardado);
    }

    @Override
    public MedicoResponseDTO obtenerPorId(Integer id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico no encontrado"));

        return medicoMapper.toDTO(medico);
    }

    @Override
    public List<MedicoResponseDTO> listar() {
        return medicoMapper.toDTOList(medicoRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        if (!medicoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico no encontrado para eliminar");
        }
        medicoRepository.deleteById(id);
    }

    @Override
    public MedicoResponseDTO obtenerPorUsuario(Integer idUsuario) {
        Medico medico = medicoRepository.findByUsuarioIdUsuario(idUsuario);
        if (medico == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe un médico asociado al usuario con id: " + idUsuario);
        }
        return medicoMapper.toDTO(medico);
    }

    // =====================================================
    // LISTADO CON DETALLE (QUERY SQL)
    // =====================================================
    @Override
    public List<MedicoListadoResponseDTO> listarMedicosDetalle() {
        List<Map<String, Object>> resultados = medicoRepository.listarMedicosDetalle();

        return resultados.stream()
                .map(row -> MedicoListadoResponseDTO.builder()
                        .id(((Number) row.get("id")).longValue())
                        .nombre(row.get("nombre") != null ? (String) row.get("nombre") : "")
                        .apellidoPaterno(row.get("apellidoPaterno") != null ? (String) row.get("apellidoPaterno") : "")
                        .apellidoMaterno(row.get("apellidoMaterno") != null ? (String) row.get("apellidoMaterno") : "")
                        .email(row.get("email") != null ? (String) row.get("email") : "")
                        .especialidades(row.get("especialidades") != null ? (String) row.get("especialidades") : "")
                        .colegiatura(row.get("colegiatura") != null ? (String) row.get("colegiatura") : "")
                        .telefono(row.get("telefono") != null ? (String) row.get("telefono") : "")
                        .horario(row.get("horario") != null ? (String) row.get("horario") : "")
                        .build()
                ).toList();
    }

    // =====================================================
    // DASHBOARD PERFIL
    // =====================================================
    @Override
    public List<PerfilMedicoDTO> listarPerfilDashboardPorMedico(Integer idMedico) {
        return medicoRepository.listarPerfilDashboardPorMedico(idMedico)
                .stream()
                .map(row -> PerfilMedicoDTO.builder()
                        .nombre1((String) row.getOrDefault("nombre1", ""))
                        .nombre2((String) row.getOrDefault("nombre2", ""))
                        .apellidoPaterno((String) row.getOrDefault("apellidoPaterno", ""))
                        .apellidoMaterno((String) row.getOrDefault("apellidoMaterno", ""))
                        .dni((String) row.getOrDefault("dni", ""))
                        .fechaNacimiento(row.get("fechaNacimiento") != null ? row.get("fechaNacimiento").toString() : "")
                        .genero((String) row.getOrDefault("genero", ""))
                        .telefono((String) row.getOrDefault("telefono", ""))
                        .direccion((String) row.getOrDefault("direccion", ""))
                        .correo((String) row.getOrDefault("correo", ""))
                        .especialidad((String) row.getOrDefault("especialidad", ""))
                        .colegiatura((String) row.getOrDefault("colegiatura", ""))
                        .horario((String) row.getOrDefault("horario", ""))
                        .build()
                ).toList();
    }

    // =====================================================
    // ACTUALIZAR PERFIL MÉDICO
    // =====================================================
    @Override
    @Transactional
    public PerfilMedicoDTO actualizarPerfil(Integer idMedico, ActualizarPerfilMedicoRequestDTO req) {

        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico no encontrado"));

        Persona persona = medico.getPersona();
        if (persona == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "El médico no tiene persona asociada.");
        }

        Usuario usuario = usuarioRepository.findById(persona.getIdPersona())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado asociado a la persona"));

        // Validar correo duplicado
        Optional<Usuario> existeCorreo = usuarioRepository.findByCorreo(req.getCorreo());
        if (existeCorreo.isPresent() && !existeCorreo.get().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está registrado por otro usuario.");
        }

        // ✅ Actualizar persona
        persona.setNombre1(req.getNombre1());
        persona.setNombre2(req.getNombre2());
        persona.setApellidoPaterno(req.getApellidoPaterno());
        persona.setApellidoMaterno(req.getApellidoMaterno());
        persona.setDni(req.getDni());
        persona.setTelefono(req.getTelefono());
        persona.setDireccion(req.getDireccion());
        persona.setGenero(req.getGenero());

        if (req.getFechaNacimiento() != null && !req.getFechaNacimiento().isBlank()) {
            try {
                persona.setFechaNacimiento(LocalDate.parse(req.getFechaNacimiento()));
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de fecha inválido. Usar yyyy-MM-dd");
            }
        }

        // ✅ Actualizar usuario
        usuario.setCorreo(req.getCorreo());

        // ✅ Actualizar médico (solo colegiatura porque horario NO existe en esta tabla)
        medico.setColegiatura(req.getColegiatura());

        personaRepository.save(persona);
        usuarioRepository.save(usuario);
        medicoRepository.save(medico);

        // Valores que no están en medico, pero se requieren en la respuesta
        String especialidadSQL = req.getEspecialidad() != null ? req.getEspecialidad() : "";
        String horarioSQL = req.getHorario() != null ? req.getHorario() : "";

        return PerfilMedicoDTO.builder()
                .nombre1(persona.getNombre1())
                .nombre2(persona.getNombre2())
                .apellidoPaterno(persona.getApellidoPaterno())
                .apellidoMaterno(persona.getApellidoMaterno())
                .dni(persona.getDni())
                .fechaNacimiento(persona.getFechaNacimiento() != null ? persona.getFechaNacimiento().toString() : "")
                .genero(persona.getGenero() != null ? persona.getGenero() : "")
                .telefono(persona.getTelefono() != null ? persona.getTelefono() : "")
                .direccion(persona.getDireccion() != null ? persona.getDireccion() : "")
                .correo(usuario.getCorreo())
                .especialidad(especialidadSQL)
                .colegiatura(medico.getColegiatura())
                .horario(horarioSQL)
                .build();
    }
}