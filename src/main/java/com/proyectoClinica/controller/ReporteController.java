package com.proyectoClinica.controller;


import com.proyectoClinica.dto.request.ReporteRequestDTO;
import com.proyectoClinica.dto.response.ReporteResponseDTO;
import com.proyectoClinica.service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<List<ReporteResponseDTO>> listar(){
        return ResponseEntity.ok(reporteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> obtenerPorId(@PathVariable Integer id){
        return ResponseEntity.ok(reporteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ReporteResponseDTO> crear(@Valid @RequestBody ReporteRequestDTO requestDTO){
        return ResponseEntity.ok(reporteService.crear(requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Reporte de Paciente - PDF
    @GetMapping("/pacientes/pdf")
    public ResponseEntity<byte[]> generarReportePacientesPDF() {
        byte[] pdfBytes = reporteService.generarReportePacientesPDF();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename("reporte_pacientes.pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    // Reporte de Paciente - Excel
    @GetMapping("/pacientes/excel")
    public ResponseEntity<byte[]> generarReportePacientesExcel() {
        byte[] excelBytes = reporteService.generarReportePacientesExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename("reporte_pacientes.xlsx")
                .build());

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

    // Reporte de Médicos - PDF
    @GetMapping("/medicos/pdf")
    public ResponseEntity<byte[]> generarReporteMedicosPDF() {
        byte[] pdfBytes = reporteService.generarReporteMedicoPDF();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename("reporte_medicos.pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    // Reporte de Médicos - Excel
    @GetMapping("/medicos/excel")
    public ResponseEntity<byte[]> generarReporteMedicosExcel() {
        byte[] excelBytes = reporteService.generarReporteMedicoExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        );
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename("reporte_medicos.xlsx")
                .build());

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

}
