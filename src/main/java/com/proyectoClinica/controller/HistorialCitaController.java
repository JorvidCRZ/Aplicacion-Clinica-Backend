package com.proyectoClinica.controller;


import com.proyectoClinica.dto.response.HistorialCitaDashboardMedicoDTO;
import com.proyectoClinica.repository.HistorialCitaRepository;
import com.proyectoClinica.service.HistorialCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/historial/citas")
@RequiredArgsConstructor
public class HistorialCitaController {
    private final HistorialCitaService historialCitaService;


    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<HistorialCitaDashboardMedicoDTO>> listarHistorialPorMedico(
            @PathVariable Integer idMedico
    ) {
        List<HistorialCitaDashboardMedicoDTO> historial =
                historialCitaService.listarHistorialCitasDashboardPorMedico(idMedico);

        return ResponseEntity.ok(historial);
    }
}
