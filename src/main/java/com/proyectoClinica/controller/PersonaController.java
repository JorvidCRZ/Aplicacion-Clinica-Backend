package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.PersonaRequestDTO;
import com.proyectoClinica.dto.response.PersonaResponseDTO;
import com.proyectoClinica.service.PersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<PersonaResponseDTO>> listar(){
        return ResponseEntity.ok(personaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaResponseDTO> obtenerPorId(@PathVariable Integer id){
        return ResponseEntity.ok(personaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PersonaResponseDTO> crear(@Valid @RequestBody PersonaRequestDTO requestDTO){
        return ResponseEntity.ok(personaService.crear(requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        personaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
