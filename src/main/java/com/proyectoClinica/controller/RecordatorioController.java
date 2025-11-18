package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.RecordatorioRequestDTO;
import com.proyectoClinica.dto.response.RecordatorioResponseDTO;
import com.proyectoClinica.service.RecordatorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/recordatorios")
@RequiredArgsConstructor
public class RecordatorioController {

    private final RecordatorioService recordatorioService;
    private final com.proyectoClinica.service.impl.RecordatorioSender recordatorioSender;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody RecordatorioRequestDTO request) {
        RecordatorioResponseDTO dto = recordatorioService.crear(request);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Recordatorio creado");
        body.put("data", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    public ResponseEntity<List<RecordatorioResponseDTO>> listar() {
        return ResponseEntity.ok(recordatorioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordatorioResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(recordatorioService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        recordatorioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<java.util.Map<String, Object>> enviarAhora(@PathVariable Integer id) {
        java.util.Map<String, Object> body = new java.util.HashMap<>();
        try {
            String resultado = recordatorioSender.enviarSync(id);
            body.put("mensaje", resultado);
            return ResponseEntity.ok(body);
        } catch (Exception ex) {
            body.put("error", ex.getMessage());
            if (ex.getCause() != null) body.put("causa", ex.getCause().toString());
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }
}
