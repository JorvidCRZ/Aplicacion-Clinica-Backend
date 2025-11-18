package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.HistorialMedicoRequestDTO;
import com.proyectoClinica.dto.response.HistorialMedicoResponseDTO;

import java.util.List;

public interface HistorialMedicoService {

    HistorialMedicoResponseDTO crear(HistorialMedicoRequestDTO requestDTO);
    HistorialMedicoResponseDTO obtenerPorId(Integer id);
    List<HistorialMedicoResponseDTO> listar();
    void eliminar(Integer id);
}
