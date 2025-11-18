package com.proyectoClinica.dto.request;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaResumenRequestDTO {
    private Integer idCita;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private Integer idMedico;
    private String especialidad;
    private String consultorio;
    private String tipo;
    private Integer duracionMinutos;
    private BigDecimal precio;
    private String motivo;
}
