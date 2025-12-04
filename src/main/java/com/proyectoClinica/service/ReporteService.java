package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.ReporteRequestDTO;
import com.proyectoClinica.dto.response.EstadisticaPacienteDTO;
import com.proyectoClinica.dto.response.ReporteGeneralDTO;
import com.proyectoClinica.dto.response.ReporteRendimientoDoctorDTO;
import com.proyectoClinica.dto.response.ReporteResponseDTO;

import java.time.LocalDate;
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

    byte[] generarReporteGeneralPDF(LocalDate inicio, LocalDate fin, int topN);

    byte[] generarReporteRendimientoDoctoresPDF();

    byte[] generarReporteEstadisticaPacientePDF();

    byte[] generarReporteUsuariosPDF();

    byte[] generarReporteCitasPDF();

    byte[] generarReporteFacturasPDF();
}
