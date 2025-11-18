package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.PersonaRequestDTO;
import com.proyectoClinica.dto.response.PersonaResponseDTO;

import java.util.List;

public interface PersonaService {

    PersonaResponseDTO crear(PersonaRequestDTO requestDTO);
    PersonaResponseDTO obtenerPorId(Integer id);
    List<PersonaResponseDTO> listar();
    void eliminar(Integer id);

}
