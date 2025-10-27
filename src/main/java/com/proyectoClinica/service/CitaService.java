package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.CitaRequestDTO;
import com.proyectoClinica.dto.response.CitaResponseDTO;

import java.util.List;

public interface CitaService {

    CitaResponseDTO crear(CitaRequestDTO requestDTO);
    CitaResponseDTO obtenerPorId(Integer id);
    List<CitaResponseDTO> listar();
    void eliminar(Integer id);
}
