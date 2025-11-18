package com.proyectoClinica.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CitaMedicoViewDTO {

    private String fecha;
    private String hora;
    private String paciente;
    private String documento;
    private String telefono;
    private String tipoConsulta;
    private String estado;
}