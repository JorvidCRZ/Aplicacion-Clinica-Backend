package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.PacienteRequestDTO;
import com.proyectoClinica.dto.response.PacienteResponseDTO;

import java.util.List;

public interface PacienteService {

    PacienteResponseDTO crear(PacienteRequestDTO requestDTO);
    PacienteResponseDTO obtenerPorId(Integer id);
    List<PacienteResponseDTO> listar();
    PacienteResponseDTO actualizar(Integer id, PacienteRequestDTO dto);
    PacienteResponseDTO obtenerPorUsuarioId(Integer idUsuario);
    void eliminar(Integer id);
}
