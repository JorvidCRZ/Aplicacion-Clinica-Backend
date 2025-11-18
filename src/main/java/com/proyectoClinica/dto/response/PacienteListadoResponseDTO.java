package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteListadoResponseDTO {
    private Long id;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String tipoDocumento;
    private String numeroDocumento;
    private String fechaNacimiento;
    private String genero;
}
