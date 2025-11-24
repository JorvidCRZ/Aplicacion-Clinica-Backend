package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.response.CitaMedicoViewDTO;
import com.proyectoClinica.dto.response.HistorialCitaDashboardMedicoDTO;
import com.proyectoClinica.repository.HistorialCitaRepository;
import com.proyectoClinica.service.HistorialCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class HistorialCitaServiceImpl implements HistorialCitaService {
    private final HistorialCitaRepository historialCitaRepository;

    @Override
    public List<HistorialCitaDashboardMedicoDTO> listarHistorialCitasDashboardPorMedico(Integer idMedico) {
        return historialCitaRepository.listarHistorialCitasDashboardPorMedico(idMedico)
                .stream()
                .map(row -> HistorialCitaDashboardMedicoDTO.builder()
                        .id_historial_cita(row.get("id_historial_cita") != null
                                ? ((Number) row.get("id_historial_cita")).intValue()
                                : 0)
                        .detalle(row.get("detalle") != null ? row.get("detalle").toString() : null)
                        .fechaEvento(row.get("fecha_historial") != null ? row.get("fecha_historial").toString() : null)
                        .paciente(row.get("nombre_paciente") != null ? row.get("nombre_paciente").toString() : null)
                        .build())
                .toList();
    }
}
