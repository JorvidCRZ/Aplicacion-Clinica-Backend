package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.CitaResumenRequestDTO;
import com.proyectoClinica.dto.response.CitaResumenResponseDTO;
import java.util.List;

public interface CitaResumenService {
    List<CitaResumenResponseDTO> listar();
    CitaResumenResponseDTO obtenerPorId(Integer id);
    CitaResumenResponseDTO registrar(CitaResumenRequestDTO dto);
    CitaResumenResponseDTO actualizar(Integer id, CitaResumenRequestDTO dto);
    void eliminar(Integer id);
}
