package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.HistorialMedicoRequestDTO;
import com.proyectoClinica.dto.request.HistorialMedicoUpdateRequestDTO;
import com.proyectoClinica.dto.response.HistorialMedicoResponseDTO;
import com.proyectoClinica.mapper.HistorialMedicoMapper;
import com.proyectoClinica.model.HistorialMedico;
import com.proyectoClinica.repository.HistorialMedicoRepository;

import java.util.List;

public interface HistorialMedicoService {

    HistorialMedicoResponseDTO crear(HistorialMedicoRequestDTO requestDTO);
    HistorialMedicoResponseDTO obtenerPorId(Integer id);
    List<HistorialMedicoResponseDTO> listar();
    void eliminar(Integer id);
    HistorialMedicoResponseDTO actualizar(Integer id, HistorialMedicoUpdateRequestDTO dto);
    List<HistorialMedicoResponseDTO> listarPorPaciente(Integer idPaciente);

    // nuevo
    List<HistorialMedicoResponseDTO> listarPorMedico(Integer idMedico);

    // nuevo
    List<HistorialMedicoResponseDTO> listarPorPacienteYMedico(Integer idPaciente, Integer idMedico);

}
