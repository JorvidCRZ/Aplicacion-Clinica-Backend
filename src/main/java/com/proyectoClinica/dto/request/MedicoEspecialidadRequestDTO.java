package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoEspecialidadRequestDTO {

    @NotNull(message = "El idMedico es obligatorio")
    @Positive(message = "El idMedico debe ser positivo")
    private Integer idMedico;

    @NotNull(message = "El idEspecialidad es obligatorio")
    @Positive(message = "El idEspecialidad debe ser positivo")
    private Integer idEspecialidad;
}
