package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
public class PagoDetalleRequestDTO {

    @NotNull(message = "El idCita es obligatorio")
    @Positive(message = "El idCita debe ser positivo")
    private Integer idCita;

    @DecimalMin(value = "0.0", inclusive = false, message = "El monto asociado debe ser mayor a 0")
    private BigDecimal montoAsociado;

    @NotNull(message = "El idPago es obligatorio")
    @Positive(message = "El idPago debe ser positivo")
    private Integer idPago;
}
