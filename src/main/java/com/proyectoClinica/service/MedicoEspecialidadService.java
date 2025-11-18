package com.proyectoClinica.service;

import com.proyectoClinica.dto.response.LlamarEspecialidadMedicoDTO;
import com.proyectoClinica.dto.response.MedicoEspecialidadResponseDTO;
import com.proyectoClinica.model.MedicoEspecialidad;

import java.util.List;

public interface MedicoEspecialidadService {

    MedicoEspecialidadResponseDTO crear(MedicoEspecialidad requestDTO);
    MedicoEspecialidadResponseDTO obtenerPorId(Integer id);
    List<MedicoEspecialidadResponseDTO> listar();
    void eliminar(Integer id);

    LlamarEspecialidadMedicoDTO obtenerEspecialidadPorIdMedico(Integer idMedico);


}
