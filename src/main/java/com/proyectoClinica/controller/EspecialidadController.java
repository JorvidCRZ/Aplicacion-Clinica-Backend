package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.EspecialidadRequestDTO;
import com.proyectoClinica.dto.response.EspecialidadResponseDTO;
import com.proyectoClinica.service.EspecialidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/especialidades")
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>> listar() {
        return ResponseEntity.ok(especialidadService.listar());
    }


    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(especialidadService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EspecialidadResponseDTO> crear(
            @Valid @RequestBody EspecialidadRequestDTO requestDTO) {
        return ResponseEntity.ok(especialidadService.crear(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody EspecialidadRequestDTO requestDTO) {
        return ResponseEntity.ok(especialidadService.actualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        especialidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
