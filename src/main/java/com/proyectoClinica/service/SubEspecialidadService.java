package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.SubEspecialidadRequestDTO;
import com.proyectoClinica.dto.response.SubEspecialidadResponseDTO;

import java.util.List;

public interface SubEspecialidadService {

    SubEspecialidadResponseDTO crear(SubEspecialidadRequestDTO requestDTO);
    SubEspecialidadResponseDTO obtenerPorId(Integer id);
    List<SubEspecialidadResponseDTO> listar();
    void eliminar(Integer id);

    // 🔹 Nuevo método
    List<SubEspecialidadResponseDTO> listarPorEspecialidad(Integer idEspecialidad);
}
