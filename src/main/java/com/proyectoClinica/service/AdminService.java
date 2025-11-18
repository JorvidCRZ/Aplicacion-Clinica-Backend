package com.proyectoClinica.service;
import com.proyectoClinica.dto.request.AdminRequestDTO;
import com.proyectoClinica.dto.response.AdminResponseDTO;

import java.util.List;
public interface AdminService {
    AdminResponseDTO crear(AdminRequestDTO requestDTO);

    AdminResponseDTO obtenerPorId(Integer id);

    List<AdminResponseDTO> listar();

    void eliminar(Integer id);
}
