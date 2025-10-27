package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteResponseDTO {

    private Integer idReporte;
    private UsuarioResponseDTO admin;
    private String tipo;
    private LocalDateTime fechaGeneracion;
    private String rutaArchivo;
}
