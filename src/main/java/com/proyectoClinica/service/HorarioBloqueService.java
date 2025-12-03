package com.proyectoClinica.service;
import com.proyectoClinica.dto.request.HorarioBloqueRequestDTO;
import com.proyectoClinica.dto.response.HorarioBloqueResponseDTO;
import com.proyectoClinica.dto.response.HorariosMedicoResponseDTO;
import com.proyectoClinica.model.HorarioBloque;

import java.time.LocalDate;
import java.util.List;

public interface HorarioBloqueService {
    HorarioBloqueResponseDTO crear(HorarioBloqueRequestDTO request);
    List<HorarioBloqueResponseDTO> listar();
    HorarioBloqueResponseDTO obtenerPorId(Integer id);
    HorarioBloqueResponseDTO actualizar(Integer id, HorarioBloqueRequestDTO request);
    void eliminar(Integer id);
    HorariosMedicoResponseDTO obtenerHorariosPorMedico(Integer idMedico);


//
//    List<HorarioBloque> obtenerBloquesDisponibles(Integer idMedicoEspecialidad, LocalDate fecha);
}
