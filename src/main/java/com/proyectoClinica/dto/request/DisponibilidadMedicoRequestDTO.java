package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadMedicoRequestDTO {

    @NotNull(message = "El idMedico es obligatorio")
    @Positive(message = "El idMedico debe ser positivo")
    private Integer idMedico;

    @NotBlank(message = "El día de la semana es obligatorio")
    @Size(max = 15, message = "El nombre de la semana no debe superar los 15 caracteres")
    private String diaSemana;

    @NotNull(message = "La hora de inicio es obligatorio")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatorio")
    private LocalTime horaFin;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no debe superar los 20 caracteres")
    private String estado;

    @Size(max = 50, message = "El nombre del turno no debe superar los 50 caracteres")
    private String nombreTurno;

    @NotNull(message = "La vigencia es obligatoria")
    private Boolean vigencia;

    @NotNull(message = "El estado del día es obligatorio")
    private Boolean diaActivo;
}
