package com.proyectoClinica.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BloquesPorDiaResponseDTO {
    private LocalDate fecha;   // Fecha concreta
    private String dia;        // Nombre del día
    private List<BloqueDTO> bloques;

    @Data
    public static class BloqueDTO {
        private LocalTime horaInicio;
        private LocalTime horaFin;
    }
}
