package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.UsuarioEditRequestDTO;
import com.proyectoClinica.dto.request.UsuarioRequestDTO;
import com.proyectoClinica.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO crear(UsuarioRequestDTO requestDTO);
    UsuarioResponseDTO obtenerPorId(Integer id);
    List<UsuarioResponseDTO> listar();
    void eliminar(Integer id);

    UsuarioResponseDTO actualizarCorreo(UsuarioEditRequestDTO requestDTO);
}
