package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.CitaRequestDTO;
import com.proyectoClinica.dto.response.CitaMedicoViewDTO;
import com.proyectoClinica.dto.response.CitaResponseDTO;
import com.proyectoClinica.dto.response.HorasEstimadasCitasDTO;

import java.time.LocalDate;
import java.util.List;

public interface CitaService {

    CitaResponseDTO crear(CitaRequestDTO requestDTO);
    CitaResponseDTO obtenerPorId(Integer id);
    List<CitaResponseDTO> listar();
    void eliminar(Integer id);

    List<CitaResponseDTO> listarCitasDeHoy();

    /*numero de citas de medico , numero de citas del medico en este mes*/

    long contarCitasPorMedico(Integer idMedico);

    long contarCitasDelMesActualPorMedico(Long idMedico);


    /*Citas por medico*/
    List<CitaMedicoViewDTO> listarCitasDashboardPorMedico(Integer idMedico);
    HorasEstimadasCitasDTO obtenerHorasYPromedioPorMedico(Integer idMedico);

}
