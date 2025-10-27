package com.proyectoClinica.dto.response;

import com.proyectoClinica.model.Pago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoDetalleResponseDTO {

    private Integer idPagoDetalle;
    private CitaResponseDTO cita;
    private BigDecimal montoAsociado;
    private Pago pago;
}
