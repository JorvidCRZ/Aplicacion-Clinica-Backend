package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoRequestDTO {

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto total debe ser superior a 0")
    private BigDecimal montoTotal;

    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 50, message = "El método de pago no debe superar los 50 caracteres")
    private String metodo;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no debe superar los 20 caracteres")
    private String estado;

    @PastOrPresent(message = "La fecha debe ser pasado o actual")
    private LocalDateTime fechaPago;
}
