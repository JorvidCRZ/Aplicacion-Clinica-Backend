package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadMedicoResponseDTO {

    private Integer idDisponibilidad;
    private MedicoResponseDTO medico;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private String nombreTurno;
    private Boolean vigencia;
    private Boolean diaActivo;
    private Integer duracionMinutos;

}
