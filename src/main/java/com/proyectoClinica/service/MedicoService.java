package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.MedicoRequestDTO;
import com.proyectoClinica.dto.response.MedicoListadoResponseDTO;
import com.proyectoClinica.dto.response.MedicoResponseDTO;

import java.util.List;

public interface MedicoService {

    MedicoResponseDTO crear(MedicoRequestDTO requestDTO);
    MedicoResponseDTO obtenerPorId(Integer id);
    List<MedicoResponseDTO> listar();
    List<MedicoListadoResponseDTO> listarMedicosDetalle();
    void eliminar(Integer id);


}
