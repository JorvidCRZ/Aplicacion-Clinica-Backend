package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.SolicitudHorarioRequestDTO;
import com.proyectoClinica.dto.request.AprobarSolicitudRequestDTO;
import com.proyectoClinica.dto.response.SolicitudHorarioResponseDTO;
import com.proyectoClinica.service.impl.SolicitudHorarioServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/solicitudes-horario")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SolicitudHorarioController {
    private final SolicitudHorarioServiceImpl solicitudHorarioService;

    // MÉDICO: Crear solicitud (solo para su propio perfil)
    @PostMapping
    public ResponseEntity<?> crearSolicitud(
            @RequestBody SolicitudHorarioRequestDTO solicitudDTO,
            @RequestHeader("X-Usuario-Id") Integer usuarioId) {
        try {
            log.info(" Médico enviando solicitud - UsuarioId: {}, MédicoId: {}",
                    usuarioId, solicitudDTO.getMedicoId());

            SolicitudHorarioResponseDTO response = solicitudHorarioService.crearSolicitud(solicitudDTO, usuarioId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("❌ Error al crear solicitud: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ADMIN: Crear solicitud para CUALQUIER médico (aprobación automática)
    @PostMapping("/admin")
    public ResponseEntity<?> crearSolicitudComoAdmin(
            @RequestBody SolicitudHorarioRequestDTO solicitudDTO,
            @RequestHeader("X-Usuario-Id") Integer adminId) {
        try {
            log.info(" Admin creando solicitud - AdminId: {}, MédicoId: {}",
                    adminId, solicitudDTO.getMedicoId());

            SolicitudHorarioResponseDTO response = solicitudHorarioService.crearSolicitudComoAdmin(solicitudDTO, adminId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("❌ Error admin creando solicitud: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ADMIN: Aprobar/Rechazar solicitud pendiente
    @PostMapping("/{solicitudId}/aprobar")
    public ResponseEntity<?> aprobarSolicitud(
            @PathVariable Long solicitudId,
            @RequestBody AprobarSolicitudRequestDTO requestDTO,
            @RequestHeader("X-Usuario-Id") Integer adminId) {
        try {
            log.info(" Admin aprobando solicitud - AdminId: {}, SolicitudId: {}, Aprobar: {}",
                    adminId, solicitudId, requestDTO.isAprobar());

            SolicitudHorarioResponseDTO response = solicitudHorarioService.aprobarSolicitud(solicitudId, requestDTO, adminId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("❌ Error al aprobar solicitud: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ADMIN: Ver todas las solicitudes pendientes
    @GetMapping("/pendientes")
    public ResponseEntity<List<SolicitudHorarioResponseDTO>> obtenerSolicitudesPendientes() {
        try {
            List<SolicitudHorarioResponseDTO> solicitudes = solicitudHorarioService.obtenerSolicitudesPendientes();
            log.info(" Obteniendo {} solicitudes pendientes", solicitudes.size());
            return ResponseEntity.ok(solicitudes);
        } catch (Exception e) {
            log.error("❌ Error al obtener solicitudes pendientes: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // MÉDICO: Ver mis propias solicitudes
    @GetMapping("/mis-solicitudes")
    public ResponseEntity<List<SolicitudHorarioResponseDTO>> obtenerMisSolicitudes(
            @RequestHeader("X-Usuario-Id") Integer usuarioId) {
        try {
            log.info(" Médico viendo sus solicitudes - UsuarioId: {}", usuarioId);

            List<SolicitudHorarioResponseDTO> solicitudes = solicitudHorarioService.obtenerSolicitudesPorUsuario(usuarioId);
            return ResponseEntity.ok(solicitudes);
        } catch (RuntimeException e) {
            log.error("❌ Error al obtener mis solicitudes: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // EXTRA: Ver todas las solicitudes (para admins)
    @GetMapping("/todas")
    public ResponseEntity<List<SolicitudHorarioResponseDTO>> obtenerTodasLasSolicitudes() {
        try {
            // Puedes implementar este método si lo necesitas
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            log.error("❌ Error al obtener todas las solicitudes: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}