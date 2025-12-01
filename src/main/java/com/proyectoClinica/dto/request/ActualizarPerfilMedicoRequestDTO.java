package com.proyectoClinica.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ActualizarPerfilMedicoRequestDTO {

    // Persona

    // Persona
    private String nombre1;
    private String nombre2;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String dni;
    private String fechaNacimiento;
    private String genero;
    private String telefono;
    private String direccion;

    // Usuario
    private String correo;

    // Médico
    private String colegiatura;

    private String especialidad;
    private String horario;
}
