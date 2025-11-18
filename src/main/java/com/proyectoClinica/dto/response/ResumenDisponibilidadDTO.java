package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenDisponibilidadDTO {

    private Integer diasActivos;
    private Integer minutosSemana;
    private Integer minutosMes;

}
