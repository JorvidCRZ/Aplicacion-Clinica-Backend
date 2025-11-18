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
public class RecordatorioRequestDTO {

    @NotNull(message = "El idCita es obligatorio")
    @Positive(message = "El idCita debe ser positivo")
    private Integer idCita;

    @FutureOrPresent(message = "La fecha de envío puede ser hoy o futura")
    private LocalDateTime fechaEnvio;

    @NotBlank(message = "El tipo de recordatorio es obligatorio")
    @Size(max = 50, message = "El tipo no debe superar los 50 caracteres")
    private String tipo;

    @NotBlank(message = "El estado del recordatorio es obligatorio")
    @Size(max = 20, message = "El estado no debe superar los 20 caracteres")
    private String estado;

    @Email(message = "Debe ingresar un correo válido")
    @Size(max = 250, message = "El correo destinatario no debe superar los 250 caracteres")
    private String destinatarioCorreo;
}
