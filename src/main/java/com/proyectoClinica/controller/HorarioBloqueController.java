package com.proyectoClinica.controller;
import com.proyectoClinica.dto.request.HorarioBloqueRequestDTO;
import com.proyectoClinica.dto.response.HorarioBloqueResponseDTO;
import com.proyectoClinica.dto.response.HorariosMedicoResponseDTO;
import com.proyectoClinica.service.HorarioBloqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horario-bloque")
@RequiredArgsConstructor
public class HorarioBloqueController {
    private final HorarioBloqueService service;

    @PostMapping
    public ResponseEntity<HorarioBloqueResponseDTO> crear(@RequestBody HorarioBloqueRequestDTO request) {
        return ResponseEntity.ok(service.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<HorarioBloqueResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioBloqueResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioBloqueResponseDTO> actualizar(@PathVariable Integer id, @RequestBody HorarioBloqueRequestDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<HorariosMedicoResponseDTO> obtenerHorariosPorMedico(
            @PathVariable Integer idMedico) {

        return ResponseEntity.ok(service.obtenerHorariosPorMedico(idMedico));
    }


}
