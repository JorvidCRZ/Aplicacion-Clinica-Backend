package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubEspecialidadRequestDTO {

    @NotNull(message = "El idEspecialidad es obligatorio")
    @Positive(message = "El idEspecialidad debe ser positivo")
    private Integer idEspecialidad;

    @NotBlank(message = "El nombre de la subespecialidad es obligatorio")
    @Size(max = 100, message = "El nombre de la subespecialidad no debe superar los 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no debe superar los 255 caracteres")
    private String descripcion;

    @Size(max = 255, message = "La URL de la imagen no debe superar los 255 caracteres")
    private String urlImg;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioSubespecialidad;
}
