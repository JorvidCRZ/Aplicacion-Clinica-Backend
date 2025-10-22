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
public class MedicoRequestDTO {

    @NotNull(message = "El idPersona es obligatorio")
    @Positive(message = "El idPersona debe ser positivo")
    private Integer idPersona;

    @NotBlank(message = "La colegiatura es obligatorio")
    @Pattern(regexp = "^[0-9]{6}$", message = "La colegiatura debe tener 6 dígitos")
    private String colegiatura;

    @Positive(message = "La experiencia debe ser positivo")
    private Integer experienciaAnios;
}
