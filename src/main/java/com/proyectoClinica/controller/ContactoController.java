package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.ContactoRequestDTO;
import com.proyectoClinica.dto.response.ContactoResponseDTO;
import com.proyectoClinica.service.ContactoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contactos")
@RequiredArgsConstructor
public class ContactoController {

    private final ContactoService contactoService;

    @PostMapping
    public ResponseEntity<ContactoResponseDTO> crear(@Valid @RequestBody ContactoRequestDTO requestDTO){
        return ResponseEntity.ok(contactoService.crear(requestDTO));
    }

    @GetMapping
    public ResponseEntity<java.util.List<ContactoResponseDTO>> listar(){
        return ResponseEntity.ok(contactoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactoResponseDTO> obtenerPorId(@PathVariable Integer id){
        return ResponseEntity.ok(contactoService.obtenerPorId(id));
    }
}
