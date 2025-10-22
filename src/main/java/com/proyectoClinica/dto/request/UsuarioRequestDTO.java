package com.proyectoClinica.dto.request;

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

    @NotNull(message = "el IdPersona es obligatorio")
    @Positive(message = "El idPersona debe ser positivo")
    private Integer idPersona;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un correo valido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña es obligatorio")
    @Size(min = 8, message = "La contraseña debe contener mínimo 8 caracteres")
    private String contrasena;

    @NotNull(message = "el idRol es obligatorio")
    @Positive(message = "El idRol debe ser positivo")
    private Integer idRol;
}
