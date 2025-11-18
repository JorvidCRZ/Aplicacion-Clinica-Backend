package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.PagoDetalleRequestDTO;
import com.proyectoClinica.dto.response.PagoDetalleResponseDTO;
import com.proyectoClinica.service.PagoDetalleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pago-detalles")
@RequiredArgsConstructor
public class PagoDetalleController {

    private final PagoDetalleService pagoDetalleService;

    @PostMapping
    public ResponseEntity<PagoDetalleResponseDTO> crear(@Valid @RequestBody PagoDetalleRequestDTO request) {
        PagoDetalleResponseDTO dto = pagoDetalleService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDetalleResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoDetalleService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PagoDetalleResponseDTO>> listar() {
        return ResponseEntity.ok(pagoDetalleService.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        pagoDetalleService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
