package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.MedicoRequestDTO;
import com.proyectoClinica.dto.response.MedicoListadoResponseDTO;
import com.proyectoClinica.dto.response.MedicoResponseDTO;
import com.proyectoClinica.mapper.MedicoMapper;
import com.proyectoClinica.model.Persona;
import com.proyectoClinica.repository.PersonaRepository;
import com.proyectoClinica.model.Medico;
import com.proyectoClinica.repository.MedicoRepository;
import com.proyectoClinica.service.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;
    private final PersonaRepository personaRepository;

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
        Medico medico = medicoRepository.findByUsuarioIdUsuario(idUsuario);

        if (medico == null) {
            throw new RuntimeException("No existe un médico asociado al usuario con id: " + idUsuario);
        }

        return medicoMapper.toDTO(medico);
    }





}
