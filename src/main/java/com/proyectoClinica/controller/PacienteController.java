package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.PacienteRequestDTO;
import com.proyectoClinica.dto.response.PacienteResponseDTO;
import com.proyectoClinica.mapper.PacienteMapper;
import com.proyectoClinica.model.Paciente;
import com.proyectoClinica.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;
    private final PacienteMapper pacienteMapper;

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> listar(){
        return ResponseEntity.ok(pacienteService.listar());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<PacienteResponseDTO> obtenerPorUsuarioId(@PathVariable Integer idUsuario) {
        PacienteResponseDTO pacienteDTO = pacienteService.obtenerPorUsuarioId(idUsuario);
        return ResponseEntity.ok(pacienteDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> obtenerPorId(@PathVariable Integer id){
        return ResponseEntity.ok(pacienteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> crear(@Valid @RequestBody PacienteRequestDTO requestDTO){
        return ResponseEntity.ok(pacienteService.crear(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody PacienteRequestDTO requestDTO) {
        return ResponseEntity.ok(pacienteService.actualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        pacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
