package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.DisponibilidadMedicoRequestDTO;
import com.proyectoClinica.dto.response.DisponibilidadMedicoResponseDTO;
import com.proyectoClinica.dto.response.ResumenDisponibilidadDTO;
import com.proyectoClinica.service.DisponibilidadMedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medicos/disponibilidad")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final DisponibilidadMedicoService disponibilidadService;

    // Crear un turno
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody DisponibilidadMedicoRequestDTO request) {
        DisponibilidadMedicoResponseDTO dto = disponibilidadService.crear(request);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Turno creado correctamente");
        body.put("data", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // Crear masivo (guardar todo)
    @PostMapping("/masivo")
    public ResponseEntity<Map<String, Object>> crearMasivo(@Valid @RequestBody List<DisponibilidadMedicoRequestDTO> requestList) {
        List<DisponibilidadMedicoResponseDTO> lista = disponibilidadService.crearMasivo(requestList);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Guardado masivo correcto");
        body.put("data", lista);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        List<DisponibilidadMedicoResponseDTO> lista = disponibilidadService.listar();
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Listado correcto");
        body.put("data", lista);
        return ResponseEntity.ok(body);
    }

    // Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Integer id) {
        DisponibilidadMedicoResponseDTO dto = disponibilidadService.obtenerPorId(id);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Encontrado");
        body.put("data", dto);
        return ResponseEntity.ok(body);
    }

    // Listar por medico
    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<Map<String, Object>> listarPorMedico(@PathVariable Integer idMedico) {
        List<DisponibilidadMedicoResponseDTO> lista = disponibilidadService.listarPorMedico(idMedico);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Turnos del médico");
        body.put("data", lista);
        return ResponseEntity.ok(body);
    }

    // Actualizar vigencia (activar/desactivar turno)
    @PatchMapping("/{id}/vigencia")
    public ResponseEntity<Map<String, Object>> actualizarVigencia(
            @PathVariable Integer id,
            @RequestParam Boolean valor) {

        disponibilidadService.actualizarVigenciaTurno(id, valor);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", valor ? "Turno activado" : "Turno desactivado");
        body.put("data", null);
        return ResponseEntity.ok(body);
    }

    // Activar/Desactivar dia completo para un médico
    @PatchMapping("/medico/{idMedico}/dia/{diaSemana}")
    public ResponseEntity<Map<String, Object>> actualizarDiaActivo(
            @PathVariable Integer idMedico,
            @PathVariable String diaSemana,
            @RequestParam Boolean activo) {

        disponibilidadService.actualizarDiaActivo(idMedico, diaSemana, activo);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", activo ? "Día activado" : "Día desactivado");
        body.put("data", null);
        return ResponseEntity.ok(body);
    }

    // Eliminar turno
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        disponibilidadService.eliminar(id);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Turno eliminado correctamente");
        body.put("data", null);
        return ResponseEntity.ok(body);
    }

    // Obtener minutos por día (ejemplo: GET /api/medicos/disponibilidad/medico/5/minutos-dia?dia=Lunes)
    @GetMapping("/medico/{idMedico}/minutos-dia")
    public ResponseEntity<Map<String, Object>> minutosPorDia(
            @PathVariable Integer idMedico,
            @RequestParam String dia) {

        int minutos = disponibilidadService.calcularMinutosPorDia(idMedico, dia);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Minutos calculados");
        body.put("data", minutos);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/medico/{idMedico}/minutos-semana")
    public ResponseEntity<Map<String, Object>> minutosSemana(@PathVariable Integer idMedico) {
        int minutos = disponibilidadService.calcularMinutosSemana(idMedico);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Minutos semanales calculados");
        body.put("data", minutos);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/medico/{idMedico}/minutos-mes")
    public ResponseEntity<Map<String, Object>> minutosMes(@PathVariable Integer idMedico) {
        int minutos = disponibilidadService.calcularMinutosMes(idMedico);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Minutos mensuales calculados");
        body.put("data", minutos);
        return ResponseEntity.ok(body);
    }

    // Resumen para el dashboard: días activos / minutos semana / minutos mes
    @GetMapping("/medico/{idMedico}/resumen")
    public ResponseEntity<Map<String, Object>> resumen(@PathVariable Integer idMedico) {
        ResumenDisponibilidadDTO resumen = disponibilidadService.obtenerResumenPorMedico(idMedico);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Resumen calculado");
        body.put("data", resumen);
        return ResponseEntity.ok(body);
    }

    // Obtener slots disponibles para una disponibilidad y fecha
    // Ejemplo: GET /api/medicos/disponibilidad/1/slots?fecha=2025-10-27
    @GetMapping("/{id}/slots")
    public ResponseEntity<Map<String, Object>> obtenerSlots(
            @PathVariable("id") Integer idDisponibilidad,
            @RequestParam("fecha") String fechaStr) {

        java.time.LocalDate fecha;
        try {
            fecha = java.time.LocalDate.parse(fechaStr);
        } catch (Exception ex) {
            Map<String, Object> body = new HashMap<>();
            body.put("mensaje", "Formato de fecha inválido. Use YYYY-MM-DD");
            body.put("data", null);
            return ResponseEntity.badRequest().body(body);
        }

        com.proyectoClinica.dto.response.SlotsDisponibilidadResponseDTO dto = disponibilidadService.obtenerSlotsDisponibles(idDisponibilidad, fecha);
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Slots calculados");
        body.put("data", dto);
        return ResponseEntity.ok(body);
    }




}
