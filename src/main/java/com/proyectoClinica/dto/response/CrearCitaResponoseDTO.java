package com.proyectoClinica.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CrearCitaResponoseDTO {

    private Integer idCita;
    private String pacienteNombre;
    private String medicoNombre;
    private String especialidad;
    private String subEspecialidad;
    private LocalDate fecha;
    private LocalTime hora;
    private BigDecimal precio;
    private String estado;
}