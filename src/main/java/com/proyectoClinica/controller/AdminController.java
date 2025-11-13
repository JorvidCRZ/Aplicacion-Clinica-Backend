package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.AdminRequestDTO;
import com.proyectoClinica.dto.response.AdminResponseDTO;
import com.proyectoClinica.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminResponseDTO> crear(@RequestBody AdminRequestDTO requestDTO) {
        return ResponseEntity.ok(adminService.crear(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<AdminResponseDTO>> listar() {
        return ResponseEntity.ok(adminService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(adminService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        adminService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
