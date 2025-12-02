package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.UsuarioEditRequestDTO;
import com.proyectoClinica.dto.request.UsuarioRequestDTO;
import com.proyectoClinica.dto.response.UsuarioResponseDTO;
import com.proyectoClinica.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar(){
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Integer id){
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO requestDTO){
        return ResponseEntity.ok(usuarioService.crear(requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/actualizar-correo")
    public ResponseEntity<UsuarioResponseDTO> actualizarCorreo(
            @Valid @RequestBody UsuarioEditRequestDTO requestDTO) {
        UsuarioResponseDTO actualizado = usuarioService.actualizarCorreo(requestDTO);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequestDTO requestDTO) {
        return ResponseEntity.ok(usuarioService.actualizar(id, requestDTO));
    }



}
