package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.PersonaRequestDTO;
import com.proyectoClinica.dto.response.PersonaResponseDTO;
import com.proyectoClinica.mapper.PersonaMapper;
import com.proyectoClinica.model.Persona;
import com.proyectoClinica.repository.PersonaRepository;
import com.proyectoClinica.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final PersonaMapper personaMapper;

    @Override
    public PersonaResponseDTO crear(PersonaRequestDTO requestDTO) {
        Persona persona = personaMapper.toEntity(requestDTO);
        Persona guardado = personaRepository.save(persona);
        return personaMapper.toDTO(guardado);
    }

    @Override
    public PersonaResponseDTO obtenerPorId(Integer id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        return personaMapper.toDTO(persona);
    }

    @Override
    public List<PersonaResponseDTO> listar() {
        return personaMapper.toDTOList(personaRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        personaRepository.deleteById(id);
    }
}
