package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.SubEspecialidadRequestDTO;
import com.proyectoClinica.dto.response.SubEspecialidadResponseDTO;
import com.proyectoClinica.service.SubEspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subespecialidades")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SubEspecialidadController {

    private final SubEspecialidadService subEspecialidadService;

    // 🔹 Obtener subespecialidades por especialidad
    @GetMapping("/subespecialidad/{idEspecialidad}")
    public List<SubEspecialidadResponseDTO> getSubespecialidadesPorEspecialidad(@PathVariable Integer idEspecialidad) {
        return subEspecialidadService.listarPorEspecialidad(idEspecialidad);
    }

    // 🔹 CRUD opcional
    @PostMapping
    public SubEspecialidadResponseDTO crear(@RequestBody SubEspecialidadRequestDTO dto) {
        return subEspecialidadService.crear(dto);
    }

    @GetMapping("/{id}")
    public SubEspecialidadResponseDTO obtenerPorId(@PathVariable Integer id) {
        return subEspecialidadService.obtenerPorId(id);
    }

    @GetMapping
    public List<SubEspecialidadResponseDTO> listar() {
        return subEspecialidadService.listar();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        subEspecialidadService.eliminar(id);
    }
}
