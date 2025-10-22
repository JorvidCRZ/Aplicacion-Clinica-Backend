package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.DisponibilidadMedicoRequestDTO;
import com.proyectoClinica.dto.response.DisponibilidadMedicoResponseDTO;

import java.util.List;

public interface DisponibilidadMedicoService {

    DisponibilidadMedicoResponseDTO crear(DisponibilidadMedicoRequestDTO requestDTO);
    DisponibilidadMedicoResponseDTO obtenerPorId(Integer id);
    List<DisponibilidadMedicoResponseDTO> listar();
    void eliminar(Integer id);
}
