package com.proyectoClinica.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

    @Valid
    @NotNull(message = "Los datos de la persona son obligatorios")
    private PersonaRequestDTO persona;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    @NotNull(message = "El idRol es obligatorio")
    private Integer idRol;

    @NotBlank(message = "El correo es obligatorio")
    private String correo;
}