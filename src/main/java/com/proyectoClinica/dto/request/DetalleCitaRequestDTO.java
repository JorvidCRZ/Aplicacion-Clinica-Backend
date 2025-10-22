package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCitaRequestDTO {

    @NotNull(message = "El idMedicoEspecialidad es obligatorio")
    @Positive(message = "El IdMedicoEspecialidad debe ser positivo")
    private Integer idMedicoEspecialidad;

    @NotNull(message = "El idSubEspecialidad es obligatorio")
    @Positive(message = "El idSubEspecialidad debe ser positivo")
    private Integer idSubEspecialidad;

    @NotNull(message = "El precio de la consulta es obligatoria")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioConsulta;

    @NotNull(message = "El precio total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio final debe ser mayor a 0")
    private BigDecimal precioTotal;
}
