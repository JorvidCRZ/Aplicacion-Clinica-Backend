package com.proyectoClinica.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaFullRequestDTO {

    @Valid
    private PacienteRequestDTO paciente;

    @Valid
    private DetalleCitaRequestDTO detalleCita;

    @Valid
    private CitaRequestDTO cita;

    // Pago opcional que se creará después de crear la cita
    @Valid
    private com.proyectoClinica.dto.request.PagoRequestDTO pago;

    // Detalles de pago opcionales; si se proveen se crearán vinculados al pago y a la cita
    @Valid
    private List<com.proyectoClinica.dto.request.PagoDetalleRequestDTO> pagoDetalles;
}
