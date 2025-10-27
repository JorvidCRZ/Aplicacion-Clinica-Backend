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
public class DetalleCitaResponseDTO {

    private Integer idDetalleCita;
    private MedicoEspecialidadResponseDTO medicoEspecialidad;
    private SubEspecialidadResponseDTO subEspecialidad;
    private BigDecimal precioConsulta;
    private BigDecimal precioTotal;
}
