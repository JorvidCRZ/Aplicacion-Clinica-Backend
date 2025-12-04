package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoCitaDTO {

    private long confirmadas;
    private long pendientes;
    private long canceladas;

    private double porcentajeConfirmadas;
    private double porcentajePendientes;
    private double porcentajeCanceladas;

}
