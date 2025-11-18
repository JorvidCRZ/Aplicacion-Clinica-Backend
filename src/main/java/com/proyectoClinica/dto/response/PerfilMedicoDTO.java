package com.proyectoClinica.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PerfilMedicoDTO {

    private String nombre1;
    private String nombre2;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String dni;
    private String fechaNacimiento;
    private String genero;
    private String telefono;
    private String direccion;
    private String correo;
    private String especialidad;
    private String colegiatura; // CMP


}
