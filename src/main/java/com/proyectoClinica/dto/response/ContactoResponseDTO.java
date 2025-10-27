package com.proyectoClinica.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContactoResponseDTO {

    private Integer idContacto;
    private String nombre;
    private String correo;
    private String telefono;
    private String tipoConsulta;
    private String asunto;
    private String mensaje;
    private LocalDateTime fecha;
    private Integer idUsuario;
}
