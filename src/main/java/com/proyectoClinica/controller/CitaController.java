package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.ActualizarEstadoCitaRequest;
import com.proyectoClinica.dto.request.CitaRequestDTO;
import com.proyectoClinica.dto.response.CitaMedicoViewDTO;
import com.proyectoClinica.dto.response.CitaResponseDTO;
import com.proyectoClinica.dto.response.HorasEstimadasCitasDTO;
import com.proyectoClinica.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<CitaResponseDTO> crearCita(@Valid @RequestBody CitaRequestDTO request) {
        CitaResponseDTO dto = citaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(citaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<java.util.List<CitaResponseDTO>> listar() {
        return ResponseEntity.ok(citaService.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hoy")
    public ResponseEntity<List<CitaResponseDTO>> listarCitasHoy(){
        return ResponseEntity.ok(citaService.listarCitasDeHoy());
    }
    /* numero de citas de medico , numero de citas del medico en este mes*/
    @GetMapping("/medico/{idMedico}/totalcitas")
    public ResponseEntity<Long> contarCitasPorMedico(@PathVariable Integer idMedico) {
        long total = citaService.contarCitasPorMedico(idMedico);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/mes/total/{idMedico}")
    public ResponseEntity<Long> contarCitasDelMesActualPorMedico(@PathVariable Long idMedico) {
        long total = citaService.contarCitasDelMesActualPorMedico(idMedico);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/dashboard/medico/{idMedico}")
    public ResponseEntity<List<CitaMedicoViewDTO>> listarCitasPorMedico(
            @PathVariable Integer idMedico
    ) {
        return ResponseEntity.ok(citaService.listarCitasDashboardPorMedico(idMedico));
    }


    @GetMapping("/dashboard/medico/{idMedico}/horas-promedio")
    public ResponseEntity<HorasEstimadasCitasDTO> obtenerHorasPromedio(
            @PathVariable Integer idMedico
    ) {
        return ResponseEntity.ok(citaService.obtenerHorasYPromedioPorMedico(idMedico));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<CitaResponseDTO> actualizarEstado(
            @PathVariable Integer id,
            @RequestBody ActualizarEstadoCitaRequest request
    ) {
        return ResponseEntity.ok(citaService.actualizarEstado(id, request));
    }
}
