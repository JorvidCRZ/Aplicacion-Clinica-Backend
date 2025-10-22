package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PacienteRequestDTO {

    @NotNull(message = "El idPersona es obligatorio")
    @Positive(message = "El IdPersona debe ser positivo")
    private Integer idPersona;

    @Positive(message = "El usuarioAgrego debe ser un número valido")
    private Integer usuarioAgrego;
}
