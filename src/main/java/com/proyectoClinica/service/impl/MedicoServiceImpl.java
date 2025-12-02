package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.ActualizarPerfilMedicoRequestDTO;
import com.proyectoClinica.dto.request.MedicoRequestDTO;
import com.proyectoClinica.dto.response.MedicoListadoResponseDTO;
import com.proyectoClinica.dto.response.MedicoResponseDTO;
import com.proyectoClinica.dto.response.PerfilMedicoDTO;
import com.proyectoClinica.mapper.MedicoMapper;
import com.proyectoClinica.model.Persona;
import com.proyectoClinica.model.Usuario;
import com.proyectoClinica.repository.PersonaRepository;
import com.proyectoClinica.model.Medico;
import com.proyectoClinica.repository.MedicoRepository;
import com.proyectoClinica.repository.UsuarioRepository;
import com.proyectoClinica.service.MedicoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public MedicoResponseDTO crear(MedicoRequestDTO requestDTO) {
        // Verificar que la persona indicada exista y asignarla al médico
        Integer idPersona = requestDTO.getIdPersona();
        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + idPersona));

        Medico medico = medicoMapper.toEntity(requestDTO);
        medico.setPersona(persona);

        Medico guardado = medicoRepository.save(medico);
        return medicoMapper.toDTO(guardado);
    }

    @Override
    public MedicoResponseDTO obtenerPorId(Integer id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medico no encontrado"));
        return medicoMapper.toDTO(medico);
    }

    @Override
    public List<MedicoResponseDTO> listar() {
        return medicoMapper.toDTOList(medicoRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        medicoRepository.deleteById(id);
    }

    @Override
    public List<MedicoListadoResponseDTO> listarMedicosDetalle() {
        List<Map<String, Object>> resultados = medicoRepository.listarMedicosDetalle();

        return resultados.stream()
                .map(row -> new MedicoListadoResponseDTO(
                        ((Number) row.get("id")).longValue(),
                        (String) row.get("nombre"),
                        (String) row.get("apellidoPaterno"),
                        (String) row.get("apellidoMaterno"),
                        (String) row.get("email"),
                        (String) row.get("especialidades"),
                        (String) row.get("colegiatura"),
                        (String) row.get("telefono"),
                        (String) row.get("horario")
                ))
                .toList();
    }
    @Override
    public MedicoResponseDTO obtenerPorUsuario(Integer idUsuario) {
        Optional<Medico> medico = medicoRepository.findByUsuarioIdUsuario(idUsuario);

        if (medico.isEmpty()) {
            throw new RuntimeException("No existe un médico asociado al usuario con id: " + idUsuario);
        }

        return medicoMapper.toDTO(medico.orElse(null));
    }

    @Override
    public List<PerfilMedicoDTO> listarPerfilDashboardPorMedico(Integer idMedico) {
        return medicoRepository.listarPerfilDashboardPorMedico(idMedico)
                .stream()
                .map(row->PerfilMedicoDTO.builder()
                        .nombre1((String) row.get("nombre1"))
                        .nombre2((String) row.get("nombre2"))
                        .apellidoPaterno((String) row.get("apellidoPaterno"))
                        .apellidoMaterno((String) row.get("apellidoMaterno"))
                        .dni((String) row.get("dni"))
                        .fechaNacimiento(
                                row.get("fechaNacimiento") != null ?
                                        row.get("fechaNacimiento").toString() : null
                        )
                        .genero((String) row.get("genero"))
                        .telefono((String) row.get("telefono"))
                        .direccion((String) row.get("direccion"))
                        .correo((String) row.get("correo"))
                        .especialidad((String) row.get("especialidad"))
                        .colegiatura((String) row.get("colegiatura"))
                        .build()
                ).toList();
    }

    @Override
    @Transactional
    public PerfilMedicoDTO actualizarPerfil(Integer idMedico,
                                            ActualizarPerfilMedicoRequestDTO req) {

        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        Persona persona = medico.getPersona();
        Usuario usuario = medico.getUsuario();

        // Actualizar persona
        persona.setNombre1(req.getNombre1());
        persona.setNombre2(req.getNombre2());
        persona.setApellidoPaterno(req.getApellidoPaterno());
        persona.setApellidoMaterno(req.getApellidoMaterno());
        persona.setDni(req.getDni());
        persona.setFechaNacimiento(LocalDate.parse(req.getFechaNacimiento()));
        persona.setGenero(req.getGenero());
        persona.setTelefono(req.getTelefono());
        persona.setDireccion(req.getDireccion());

        // Actualizar usuario
        usuario.setCorreo(req.getCorreo());

        // Actualizar médico
        medico.setColegiatura(req.getColegiatura());

        // Especialidad (si tienes tabla intermedia medico_especialidad)
        if(req.getEspecialidad() != null){
            medicoRepository.actualizarEspecialidad(idMedico, req.getEspecialidad());
        }

        // Guardar
        personaRepository.save(persona);
        usuarioRepository.save(usuario);
        medicoRepository.save(medico);

        // Retornar perfil actualizado
        return this.listarPerfilDashboardPorMedico(idMedico).get(0);
    }



}
