//package com.proyectoClinica.controller;
//
//import com.proyectoClinica.dto.request.BloqueDisponibleDTO;
//import com.proyectoClinica.dto.response.LlamarEspecialidadMedicoDTO;
//import com.proyectoClinica.service.MedicoEspecialidadService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDate;
//
//@RestController
//@RequestMapping("/api/citas")
//@RequiredArgsConstructor
//public class CitaHelperController {
//
//    private final MedicoEspecialidadService medicoEspecialidadService;
//    private final BloqueService bloqueService;
//
//    @GetMapping("/bloque-disponible")
//    public ResponseEntity<BloqueDisponibleDTO> obtenerBloqueDisponible(
//            @RequestParam Integer idMedico,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
//
//        // 1️⃣ Obtiene el primer idMedicoEspecialidad del médico
//        LlamarEspecialidadMedicoDTO medicoEspecialidad =
//                medicoEspecialidadService.obtenerEspecialidadPorIdMedico(idMedico);
//
//        // 2️⃣ Obtiene el primer bloque disponible para esa fecha
//        Integer idBloque = bloqueService.obtenerPrimerBloqueDisponible(
//                medicoEspecialidad.getIdMedicoEspecialidad(), fecha);
//
//        // 3️⃣ Devuelve DTO con todo listo para frontend
//        BloqueDisponibleDTO response = BloqueDisponibleDTO.builder()
//                .idMedicoEspecialidad(medicoEspecialidad.getIdMedicoEspecialidad())
//                .idBloque(idBloque)
//                .nombreEspecialidad(medicoEspecialidad.getNombreEspecialidad())
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
//}
