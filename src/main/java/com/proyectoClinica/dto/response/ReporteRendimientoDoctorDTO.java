package com.proyectoClinica.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteRendimientoDoctorDTO {
    private String nombreDoctor;
    private String especialidad;
    private long totalCitas;
    private long citasCompletadas;
    private long citasCanceladas;
    private long citasPendientes;
    private BigDecimal ingresosTotales;
}
