package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.ReporteRequestDTO;
import com.proyectoClinica.dto.response.ReporteResponseDTO;

import java.util.List;

public interface ReporteService {

    ReporteResponseDTO crear(ReporteRequestDTO requestDTO);
    ReporteResponseDTO obtenerPorId(Integer id);
    List<ReporteResponseDTO> listar();
    void eliminar(Integer id);

    byte[] generarReportePacientesPDF();
    byte[] generarReportePacientesExcel();

    byte[] generarReporteMedicoPDF();
    byte[] generarReporteMedicoExcel();
}
