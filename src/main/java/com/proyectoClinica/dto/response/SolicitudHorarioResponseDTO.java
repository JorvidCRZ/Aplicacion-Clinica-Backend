package com.proyectoClinica.dto.response;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
public class SolicitudHorarioResponseDTO {
    private Long id;
    private Integer medicoId;
    private String medicoNombre;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private String motivo;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaAprobacion;
    private String comentariosAdmin;
}
