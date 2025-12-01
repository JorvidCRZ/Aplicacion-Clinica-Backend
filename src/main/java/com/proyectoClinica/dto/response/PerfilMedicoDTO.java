package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor  // ✅ recomendado agregar
@AllArgsConstructor // ✅ recomendado agregar
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
    private String especialidad; // ✅ AGREGAR
    private String colegiatura;
    private String horario;     // ✅ FALTABA ESTE CAMPO

}
