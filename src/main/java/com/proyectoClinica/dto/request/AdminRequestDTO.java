package com.proyectoClinica.dto.request;

import lombok.Data;

@Data
public class AdminRequestDTO {
    private Integer idPersona;
    private Integer idUsuario;
    private Integer idUsuarioAgrego;
    private String cargo;
    private String nivelAcceso;
    private String estado;
}