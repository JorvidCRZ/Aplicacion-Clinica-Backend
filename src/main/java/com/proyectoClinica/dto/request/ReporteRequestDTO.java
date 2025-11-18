package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteRequestDTO {

    @NotNull(message = "El id del administrador es obligatorio")
    @Positive(message = "El id del administrador debe ser positivo")
    private Integer admin;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 50, message = "El tipo no debe superar los 50 caracteres" )
    private String tipo;
}
