package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.PacienteRequestDTO;
import com.proyectoClinica.dto.response.PacienteResponseDTO;
import com.proyectoClinica.mapper.PacienteMapper;
import com.proyectoClinica.model.Paciente;
import com.proyectoClinica.model.Persona;
import com.proyectoClinica.repository.PacienteRepository;
import com.proyectoClinica.repository.PersonaRepository;
import com.proyectoClinica.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;
    private final PersonaRepository personaRepository;

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
    public void eliminar(Integer id) {
        pacienteRepository.deleteById(id);
    }
}
