package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotsDisponibilidadResponseDTO {
    private Integer idDisponibilidad;
    private LocalDate fecha;
    private List<String> slotsDisponibles; // formato HH:mm
    private List<String> slotsOcupados; // formato HH:mm
}
