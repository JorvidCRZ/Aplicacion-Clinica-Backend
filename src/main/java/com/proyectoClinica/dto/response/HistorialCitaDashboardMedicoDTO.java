package com.proyectoClinica.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistorialCitaDashboardMedicoDTO {

    private int id_historial_cita;
    private String detalle;             // hc.detalle
    private String fechaEvento;         // hc.fecha
    private String paciente;            // nombre completo del paciente
}
