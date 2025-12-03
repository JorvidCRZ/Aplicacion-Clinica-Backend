package com.proyectoClinica.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloqueDisponibleDTO {
    private Integer idMedicoEspecialidad;
    private Integer idBloque;
    private String nombreEspecialidad;
}
