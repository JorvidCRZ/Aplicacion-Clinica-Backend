package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.ContactoRequestDTO;
import com.proyectoClinica.dto.response.ContactoResponseDTO;

public interface ContactoService {

    ContactoResponseDTO crear(ContactoRequestDTO requestDTO);
    
    ContactoResponseDTO obtenerPorId(Integer id);

    java.util.List<ContactoResponseDTO> listar();

}
