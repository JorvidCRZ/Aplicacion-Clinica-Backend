package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.HistorialMedicoRequestDTO;
import com.proyectoClinica.dto.request.HistorialMedicoUpdateRequestDTO;
import com.proyectoClinica.dto.response.HistorialMedicoResponseDTO;
import com.proyectoClinica.service.HistorialMedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historial-medico")
@RequiredArgsConstructor
public class HistorialMedicoController {

    private final HistorialMedicoService historialMedicoService;

    @PostMapping
    public ResponseEntity<HistorialMedicoResponseDTO> crear(
            @Valid @RequestBody HistorialMedicoRequestDTO dto
    ) {
        return ResponseEntity.ok(historialMedicoService.crear(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialMedicoResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(historialMedicoService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<HistorialMedicoResponseDTO>> listar() {
        return ResponseEntity.ok(historialMedicoService.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        historialMedicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HistorialMedicoResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody HistorialMedicoUpdateRequestDTO dto
    ) {
        return ResponseEntity.ok(historialMedicoService.actualizar(id, dto));
    }


    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<HistorialMedicoResponseDTO>> listarPorPaciente(
            @PathVariable Integer idPaciente
    ) {
        return ResponseEntity.ok(historialMedicoService.listarPorPaciente(idPaciente));
    }

    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<HistorialMedicoResponseDTO>> listarPorMedico(
            @PathVariable Integer idMedico
    ) {
        return ResponseEntity.ok(historialMedicoService.listarPorMedico(idMedico));
    }

    @GetMapping("/paciente/{idPaciente}/medico/{idMedico}")
    public ResponseEntity<List<HistorialMedicoResponseDTO>> listarPorPacienteYMedico(
            @PathVariable Integer idPaciente,
            @PathVariable Integer idMedico
    ) {
        return ResponseEntity.ok(historialMedicoService.listarPorPacienteYMedico(idPaciente, idMedico));
    }


}
