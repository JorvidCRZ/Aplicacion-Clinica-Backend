package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubEspecialidadResponseDTO {

    private Integer idSubespecialidad;
    private EspecialidadResponseDTO especialidad;
    private String nombre;
    private String descripcion;
    private String urlImg;
    private BigDecimal precioSubespecial;
}
