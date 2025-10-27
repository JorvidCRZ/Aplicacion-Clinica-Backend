package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspecialidadResponseDTO {

    private Integer idEspecialidad;
    private String nombre;
    private String descripcion;
    private String urlImgIcono;
    private String urlImgPort;
    private String descripcionPortada;
}
