package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.DetalleCitaRequestDTO;
import com.proyectoClinica.dto.response.DetalleCitaResponseDTO;
import com.proyectoClinica.service.DetalleCitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle-citas")
@RequiredArgsConstructor
public class DetalleCitaController {

    private final DetalleCitaService detalleCitaService;

    @PostMapping
    public ResponseEntity<DetalleCitaResponseDTO> crear(@Valid @RequestBody DetalleCitaRequestDTO request) {
        DetalleCitaResponseDTO dto = detalleCitaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleCitaResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(detalleCitaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<DetalleCitaResponseDTO>> listar() {
        return ResponseEntity.ok(detalleCitaService.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        detalleCitaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
