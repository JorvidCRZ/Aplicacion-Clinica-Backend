package com.proyectoClinica.service;

import com.proyectoClinica.dto.response.CitaMedicoViewDTO;
import com.proyectoClinica.dto.response.HistorialCitaDashboardMedicoDTO;

import java.util.List;

public interface HistorialCitaService {
    List<HistorialCitaDashboardMedicoDTO> listarHistorialCitasDashboardPorMedico(Integer idMedico);
}
