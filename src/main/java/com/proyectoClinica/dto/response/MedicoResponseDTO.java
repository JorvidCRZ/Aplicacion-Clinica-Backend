package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoResponseDTO {

    private Integer idMedico;
    private PersonaResponseDTO persona;
    private String colegiatura;
    private Integer experienciaAnios;
    private String email;
    private String horario;
    private String especialidad;
}