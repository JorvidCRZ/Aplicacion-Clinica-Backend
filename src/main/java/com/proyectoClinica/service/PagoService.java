package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.PagoRequestDTO;
import com.proyectoClinica.dto.response.PagoResponseDTO;

import java.util.List;

public interface PagoService {

    PagoResponseDTO crear(PagoRequestDTO requestDTO);
    PagoResponseDTO obtenerPorId(Integer id);
    List<PagoResponseDTO> listar();
    void eliminar(Integer id);
}
