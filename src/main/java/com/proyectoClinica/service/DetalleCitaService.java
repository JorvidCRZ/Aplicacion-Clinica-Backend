package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.DetalleCitaRequestDTO;
import com.proyectoClinica.dto.response.DetalleCitaResponseDTO;

import java.util.List;

public interface DetalleCitaService {

    DetalleCitaResponseDTO crear(DetalleCitaRequestDTO requestDTO);
    DetalleCitaResponseDTO obtenerPorId(Integer id);
    List<DetalleCitaResponseDTO> listar();
    void eliminar(Integer id);
}
