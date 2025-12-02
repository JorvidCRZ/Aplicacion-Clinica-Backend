package com.proyectoClinica.controller;
import com.proyectoClinica.dto.response.NotificacionResponseDTO;
import com.proyectoClinica.service.impl.NotificacionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NotificacionController {
    private final NotificacionServiceImpl notificacionService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerNotificacionesUsuario(@PathVariable Integer usuarioId) {
        List<NotificacionResponseDTO> notificaciones = notificacionService.obtenerNotificacionesUsuario(usuarioId);
        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/no-leidas")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerNotificacionesNoLeidas(
            @RequestHeader("X-Usuario-Id") Integer usuarioId) {
        List<NotificacionResponseDTO> notificaciones = notificacionService.obtenerNotificacionesNoLeidas(usuarioId);
        return ResponseEntity.ok(notificaciones);
    }

    @PostMapping("/{notificacionId}/marcar-leida")
    public ResponseEntity<?> marcarComoLeida(
            @PathVariable Long notificacionId,
            @RequestHeader("X-Usuario-Id") Integer usuarioId) {
        try {
            notificacionService.marcarComoLeida(notificacionId, usuarioId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/contador-no-leidas")
    public ResponseEntity<Map<String, Long>> contarNotificacionesNoLeidas(
            @RequestHeader("X-Usuario-Id") Integer usuarioId) {
        Long contador = notificacionService.contarNotificacionesNoLeidas(usuarioId);
        return ResponseEntity.ok(Map.of("contador", contador));
    }
}

