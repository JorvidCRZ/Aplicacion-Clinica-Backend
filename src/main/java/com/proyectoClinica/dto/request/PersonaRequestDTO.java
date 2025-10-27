package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe contener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe contener entre 2 y 50 caracteres")
    private String apellido;

    @Pattern(regexp = "\\d{8}", message = "El dni debe contener 8 dígitos")
    private String dni;

    @Past(message = "La fecha de nacimiento tiene que ser pasado")
    private LocalDate fechaNacimiento;

    @Pattern(regexp = "^[0-9+ ]{9,12}$", message = "El teléfono debe contener entre 9 y 12 caracteres numéricos")
    private String telefono;
}
