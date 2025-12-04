package com.proyectoClinica.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DisponibilidadDashboardResponse {
    private Integer id;
    private String medico;
    private String especialidad;
    private String dias;
    private String horaInicio;
    private String horaFin;
    private Integer   duracion;
    private Integer bloques;
    private String estado;
}
