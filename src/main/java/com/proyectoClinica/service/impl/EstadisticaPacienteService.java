package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.response.EstadisticaPacienteDTO;
import com.proyectoClinica.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EstadisticaPacienteService {

    private final PacienteRepository pacienteRepository;

    public List<EstadisticaPacienteDTO> obtenerEstadisticasPacientes() {
        List<Map<String, Object>> results = pacienteRepository.estadisticasPacientes();
        return results.stream().map(row -> EstadisticaPacienteDTO.builder()
                .mes((String) row.get("mes"))
                .nuevosRegistros(((Number) row.get("nuevos_registros")).longValue())
                .citasPromedio(((Number) row.get("citas_promedio")).doubleValue())
                .build()
        ).toList();
    }
}
