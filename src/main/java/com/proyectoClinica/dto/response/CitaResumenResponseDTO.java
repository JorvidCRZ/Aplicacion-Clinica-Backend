package com.proyectoClinica.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaResumenResponseDTO {
    private Integer idCitaResumen;
    private Integer idCita;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private Integer idMedico;
    private String nombreMedico;
    private String especialidad;
    private String consultorio;
    private String tipo;
    private Integer duracionMinutos;
    private BigDecimal precio;
    private String motivo;
}
