package com.proyectoClinica.controller;


import com.proyectoClinica.dto.request.ReporteRequestDTO;
import com.proyectoClinica.dto.response.ReporteGeneralDTO;
import com.proyectoClinica.dto.response.ReporteRendimientoDoctorDTO;
import com.proyectoClinica.dto.response.ReporteResponseDTO;
import com.proyectoClinica.service.ReporteService;
import com.proyectoClinica.service.export.ReporteRendimientoDoctorPDFExporter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    // Reporte General - PDF
    @GetMapping("/general/pdf")
    public ResponseEntity<byte[]> generarReporteGeneralPDF(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fin,
            @RequestParam(defaultValue = "5") int topN
    ) {
        byte[] pdfBytes = reporteService.generarReporteGeneralPDF(inicio, fin, topN);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename("reporte_general.pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    // Reporte Rendimiento Doctores - PDF
    @GetMapping("/doctores-rendimiento/pdf")
    public ResponseEntity<byte[]> getPdfRendimientoDoctores() {
        byte[] pdf = reporteService.generarReporteRendimientoDoctoresPDF();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_rendimiento_doctores.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // Reporte Estadistica Pacientes - PDF
    @GetMapping("/estadistica-pacientes/pdf")
    public ResponseEntity<byte[]> getReporteEstadisticaPacientesPDF() {
        byte[] pdf = reporteService.generarReporteEstadisticaPacientePDF();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=estadistica_pacientes.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // Reporte  Usuarios - PDF
    @GetMapping("/usuarios/pdf")
    public void generarReporteUsuariosPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String fileName = "Reporte_Usuarios_" +
                java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);

        byte[] pdfBytes = reporteService.generarReporteUsuariosPDF();

        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }

    // Reporte Citas - PDF
    @GetMapping("/citas/pdf")
    public void generarReporteCitasPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String fileName = "Reporte_Citas_" +
                java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);

        byte[] pdfBytes = reporteService.generarReporteCitasPDF();

        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }

    // Reporte Facturas - PDF
    @GetMapping("/facturas/pdf")
    public void generarReporteFacturasPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String fileName = "Reporte_Facturas_" +
                java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);

        byte[] pdfBytes = reporteService.generarReporteFacturasPDF();

        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }
}
