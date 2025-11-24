package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HorasEstimadasCitasDTO {
    private Double horasTotales;
    private Double promedioMinutos;
}
