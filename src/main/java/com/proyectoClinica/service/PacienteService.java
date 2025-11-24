package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.PacienteRequestDTO;
import com.proyectoClinica.dto.response.*;


import java.util.List;

public interface PacienteService {

    PacienteResponseDTO crear(PacienteRequestDTO requestDTO);
    PacienteResponseDTO obtenerPorId(Integer id);
    List<PacienteResponseDTO> listar();
    PacienteResponseDTO actualizar(Integer id, PacienteRequestDTO dto);
    PacienteResponseDTO obtenerPorUsuarioId(Integer idUsuario);
    List<PacienteListadoResponseDTO> listarPacientesDetalle();
    void eliminar(Integer id);

    /*Tabla Paciente en dashboard Medico*/
    List<PacienteDashboardDTO> listarDashboardPorMedico(Integer idMedico);

    /*Para calcular la puntualidad*/
    /* Calcular puntualidad */
    PuntualidadDTO obtenerPuntualidadMedico(Integer idMedico);
    /* Calcular satisfacción */
    SatisfaccionDTO obtenerSatisfaccionMedico(Integer idMedico);

}
