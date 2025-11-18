package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteResponseDTO {

    private Integer idPaciente;
    private PersonaResponseDTO persona;
    private UsuarioResponseDTO usuarioAgrego;
    private String tipoSangre;
    private Double peso;
    private Double altura;
    private String contactoEmergenciaNombre;
    private String contactoEmergenciaRelacion;
    private String contactoEmergenciaTelefono;
    private String email;
}
