package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.EspecialidadRequestDTO;
import com.proyectoClinica.dto.response.EspecialidadResponseDTO;
import java.util.List;

public interface EspecialidadService {

    EspecialidadResponseDTO crear(EspecialidadRequestDTO requestDTO);
    EspecialidadResponseDTO actualizar(Integer id, EspecialidadRequestDTO requestDTO);
    EspecialidadResponseDTO obtenerPorId(Integer id);
    List<EspecialidadResponseDTO> listar();
    void eliminar(Integer id);
}
