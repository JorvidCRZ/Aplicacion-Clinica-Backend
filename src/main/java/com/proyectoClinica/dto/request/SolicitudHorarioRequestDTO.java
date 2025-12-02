package com.proyectoClinica.dto.request;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class SolicitudHorarioRequestDTO {
    private Integer medicoId;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String motivo;
}

