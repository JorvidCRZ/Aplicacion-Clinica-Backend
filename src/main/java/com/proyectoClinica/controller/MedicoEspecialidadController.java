package com.proyectoClinica.controller;

import com.proyectoClinica.dto.response.LlamarEspecialidadMedicoDTO;
import com.proyectoClinica.dto.response.MedicoEspecialidadResponseDTO;
import com.proyectoClinica.model.MedicoEspecialidad;
import com.proyectoClinica.service.MedicoEspecialidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medico-especialidades")
@RequiredArgsConstructor
public class MedicoEspecialidadController {

    private final MedicoEspecialidadService medicoEspecialidadService;

    @PostMapping
    public ResponseEntity<MedicoEspecialidadResponseDTO> crear(@Valid @RequestBody MedicoEspecialidad request) {
        MedicoEspecialidadResponseDTO dto = medicoEspecialidadService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoEspecialidadResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(medicoEspecialidadService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<MedicoEspecialidadResponseDTO>> listar() {
        return ResponseEntity.ok(medicoEspecialidadService.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        medicoEspecialidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/especialidad/{idMedico}")
    public LlamarEspecialidadMedicoDTO obtenerEspecialidadPorMedico(
            @PathVariable Integer idMedico
    ) {
        return medicoEspecialidadService.obtenerEspecialidadPorIdMedico(idMedico);
    }
}
