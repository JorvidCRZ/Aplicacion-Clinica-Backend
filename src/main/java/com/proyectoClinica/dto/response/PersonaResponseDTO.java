package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaResponseDTO {

    private Integer idPersona;
    private String tipoDocumento;
    private String nombre1;
    private String nombre2;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String dni;
    private LocalDate fechaNacimiento;
    private String genero;
    private String pais;
    private String departamento;
    private String provincia;
    private String distrito;
    private String telefono;
    private String direccion;
}
