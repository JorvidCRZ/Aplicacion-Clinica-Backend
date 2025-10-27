package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaResponseDTO {

    private Integer idCita;
    private PacienteResponseDTO paciente;
    private DetalleCitaResponseDTO detalleCita;
    private DisponibilidadMedicoResponseDTO disponibilidad;
    private LocalDate fechaCita;
    private LocalTime horaCita;
    private String estado;
    private String motivoConsulta;
}
