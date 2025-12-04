package com.proyectoClinica.dto.request;

import lombok.Data;

@Data
public class CrearCitaRequestDTO {
    private Integer idPaciente;
    private Integer idMedicoEspecialidad;
    private Integer idSubEspecialidad;
    private Integer idBloque;
    private String motivoConsulta;
}
