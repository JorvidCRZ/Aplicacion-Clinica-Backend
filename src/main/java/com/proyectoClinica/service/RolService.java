package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.RolRequestDTO;
import com.proyectoClinica.dto.response.RolResponseDTO;

import java.util.List;

public interface RolService {

    RolResponseDTO crear(RolRequestDTO requestDTO);
    RolResponseDTO obtenerPorId(Integer id);
    List<RolResponseDTO> listar();
    void eliminar(Integer id);
}
