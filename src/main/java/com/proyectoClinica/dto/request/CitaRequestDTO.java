package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaRequestDTO {

    @NotNull(message = "El idPaciente es obligatorio")
    @Positive(message = "El idPaciente debe ser positivo")
    private Integer idPaciente;

    @NotNull(message = "El idDetalleCita es obligatorio")
    @Positive(message = "El idDetalleCita debe ser positivo")
    private Integer idDetalleCita;

    @NotNull(message = "El idDisponibilidad es obligatorio")
    @Positive(message = "El idDisponibilidad debe ser positivo")
    private Integer idDisponibilidad;

    @NotNull(message = "La fecha de la cita es obligatoria")
    @FutureOrPresent(message = "La fecha de la cita de ser hoy o una fecha futura")
    private LocalDate fechaCita;

    @NotNull(message = "La hora de la cita es obligatoria")
    private LocalTime horaCita;

    @NotBlank(message = "El estado de la cita es obligatorio")
    @Size(max = 20, message = "El estado de la cita no debe superar los 20 caracteres")
    private String estado;

    @NotBlank(message = "El motivo de la consulta es obligatorio")
    @Size(max = 255, message = "El motivo de la consulta no debe superar los 255 caracteres")
    private String motivoConsulta;
}
