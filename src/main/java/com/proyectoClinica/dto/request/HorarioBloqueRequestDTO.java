package com.proyectoClinica.dto.request;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioBloqueRequestDTO {
    private Integer idDisponibilidad;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean disponible;
    private Integer idCita;
    private Integer duracionMinutos;
}
