package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoEspecialidadResponseDTO {

    private Integer idMedicoEspecialidad;
    private MedicoResponseDTO medico;
    private EspecialidadResponseDTO especialidad;
}
