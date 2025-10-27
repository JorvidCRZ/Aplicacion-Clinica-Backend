package com.proyectoClinica.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Endpoint de login simulado (sin seguridad)
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody LoginRequest request) {
        // TODO: implementar autenticación real
        Map<String,Object> body = new HashMap<>();
        // Aquí puedes devolver el id_medico simulado o un token en el futuro
        body.put("mensaje", "Login simulado - implementar autenticación");
        body.put("data", request); // para pruebas
        return ResponseEntity.ok(body);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class LoginRequest {
        private String correo;
        private String contrasena;
    }
}
