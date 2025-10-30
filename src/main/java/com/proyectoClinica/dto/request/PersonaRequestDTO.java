package com.proyectoClinica.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaRequestDTO {

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El primer nombre debe contener entre 2 y 50 caracteres")
    private String nombre1;

    @Size(max = 50, message = "El segundo nombre no debe superar los 50 caracteres")
    private String nombre2;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido paterno debe contener entre 2 y 50 caracteres")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido materno debe contener entre 2 y 50 caracteres")
    private String apellidoMaterno;

    @Pattern(regexp = "\\d{8}", message = "El DNI debe contener 8 dígitos")
    private String dni;

    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;

    private String genero;
    private String pais;
    private String departamento;
    private String provincia;
    private String distrito;

    @Size(max = 255, message = "La dirección no debe exceder los 255 caracteres")
    private String direccion;

    @Pattern(regexp = "^[0-9+ ]{9,12}$", message = "El teléfono debe contener entre 9 y 12 caracteres numéricos")
    private String telefono;
}
