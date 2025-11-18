package com.proyectoClinica.dto.response;

import lombok.Data;

@Data
public class AdminResponseDTO {
    private Integer idAdmin;
    private String nombreCompleto;
    private String cargo;
    private String nivelAcceso;
    private String estado;
    private Long personaId;
    private Long usuarioId;
    private Long usuarioAgregoId;
}

