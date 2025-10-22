package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialMedicoRequestDTO {

    @NotNull(message = "El idPaciente es obligatoria")
    @Positive(message = "El idPaciente debe ser positivo")
    private Integer idPaciente;

    @NotNull(message = "El idCita es obligatoria")
    @Positive(message = "El idCita debe ser positivo")
    private Integer idCita;

    @NotNull(message = "El icMedico es obligatorio")
    @Positive(message = "El idMedico debe ser positivo")
    private Integer idMedico;

    @NotBlank(message = "El diagnóstico no puede estar vacío")
    private String diagnostico;

    @Size(max = 500, message = "Las observaciones no deben superar los 500 caracteres")
    private String observaciones;

    @Size(max = 500, message = "La receta no debe superar los 500 caracteres")
    private String receta;

    @PastOrPresent(message = "La fecha debe ser pasado o presente")
    private LocalDateTime fecha;
}
