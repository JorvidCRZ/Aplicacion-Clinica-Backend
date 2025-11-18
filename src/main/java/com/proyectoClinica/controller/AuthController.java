package com.proyectoClinica.controller;

//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    // Endpoint de login simulado (sin seguridad)
//    @PostMapping("/login")
//    public ResponseEntity<Map<String,Object>> login(@RequestBody LoginRequest request) {
//        // TODO: implementar autenticación real
//        Map<String,Object> body = new HashMap<>();
//        // Aquí puedes devolver el id_medico simulado o un token en el futuro
//        body.put("mensaje", "Login simulado - implementar autenticación");
//        body.put("data", request); // para pruebas
//        return ResponseEntity.ok(body);
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    static class LoginRequest {
//        private String correo;
//        private String contrasena;
//    }
//}

import com.proyectoClinica.dto.request.LoginRequestDTO;
import com.proyectoClinica.dto.response.LoginResponseDTO;
import com.proyectoClinica.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            // 🔹 Intentar autenticar
            LoginResponseDTO response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // 🔹 Si el usuario no existe o la contraseña es incorrecta
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            // 🔹 Si ocurre otro tipo de error (DB, null, etc.)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensaje", "Error interno del servidor"));
        }
    }
}