package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticaPacienteDTO {
    private String mes;
    private Long nuevosRegistros;
    private Double citasPromedio;
    //private Double frecuenciaTratamientos;
}
