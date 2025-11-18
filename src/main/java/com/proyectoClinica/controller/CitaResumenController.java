package com.proyectoClinica.controller;
import com.proyectoClinica.dto.request.CitaResumenRequestDTO;
import com.proyectoClinica.dto.response.CitaResumenResponseDTO;
import com.proyectoClinica.service.CitaResumenService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/cita-resumen")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CitaResumenController {

    private final CitaResumenService citaResumenService;

    @GetMapping
    public ResponseEntity<List<CitaResumenResponseDTO>> listar() {
        return ResponseEntity.ok(citaResumenService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResumenResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(citaResumenService.obtenerPorId(id));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CitaResumenResponseDTO> registrar(@Valid @RequestBody CitaResumenRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaResumenService.registrar(dto));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<CitaResumenResponseDTO> actualizar(@PathVariable Integer id,
                                                             @Valid @RequestBody CitaResumenRequestDTO dto) {
        return ResponseEntity.ok(citaResumenService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        citaResumenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
