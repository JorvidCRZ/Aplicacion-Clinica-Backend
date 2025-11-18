package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialMedicoResponseDTO {

    private Integer idHistorial;
    private PacienteResponseDTO paciente;
    private CitaResponseDTO cita;
    private MedicoResponseDTO medico;
    private String diagnostico;
    private String observaciones;
    private String receta;
    private LocalDateTime fecha;
}
