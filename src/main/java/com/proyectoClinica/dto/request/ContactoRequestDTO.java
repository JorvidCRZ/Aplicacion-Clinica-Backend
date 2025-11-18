package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactoRequestDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String correo;

    @NotBlank
    private String mensaje;

    // Optional fields for new DB columns
    private String telefono;
    private String tipoConsulta;
    private String asunto;

    // Optional: id of the user if the contact comes from a logged user
    private Integer idUsuario;
}
