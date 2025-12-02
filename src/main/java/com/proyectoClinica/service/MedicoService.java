package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.ActualizarPerfilMedicoRequestDTO;
import com.proyectoClinica.dto.request.MedicoRequestDTO;
import com.proyectoClinica.dto.response.CitaMedicoViewDTO;
import com.proyectoClinica.dto.response.MedicoListadoResponseDTO;
import com.proyectoClinica.dto.response.MedicoResponseDTO;
import com.proyectoClinica.dto.response.PerfilMedicoDTO;

import java.util.List;

public interface MedicoService {

    MedicoResponseDTO crear(MedicoRequestDTO requestDTO);
    MedicoResponseDTO obtenerPorId(Integer id);
    List<MedicoResponseDTO> listar();
    List<MedicoListadoResponseDTO> listarMedicosDetalle();
    void eliminar(Integer id);
    MedicoResponseDTO obtenerPorUsuario(Integer idUsuario);


    List<PerfilMedicoDTO> listarPerfilDashboardPorMedico(Integer idMedico);

    PerfilMedicoDTO actualizarPerfil(Integer idMedico, ActualizarPerfilMedicoRequestDTO request);


}
