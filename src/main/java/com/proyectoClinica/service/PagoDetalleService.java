package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.PagoDetalleRequestDTO;
import com.proyectoClinica.dto.response.PagoDetalleResponseDTO;

import java.util.List;

public interface PagoDetalleService {

    PagoDetalleResponseDTO crear(PagoDetalleRequestDTO requestDTO);
    PagoDetalleResponseDTO obtenerPorId(Integer id);
    List<PagoDetalleResponseDTO> listar();
    void eliminar(Integer id);
}
