package com.proyectoClinica.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> handleBadRequest(IllegalArgumentException ex) {
        Map<String,Object> body = new HashMap<>();
        body.put("mensaje", ex.getMessage());
        body.put("data", null);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,Object>> handleRuntime(RuntimeException ex) {
        Map<String,Object> body = new HashMap<>();
        body.put("mensaje", ex.getMessage());
        body.put("data", null);
        return ResponseEntity.status(500).body(body);
    }
}
