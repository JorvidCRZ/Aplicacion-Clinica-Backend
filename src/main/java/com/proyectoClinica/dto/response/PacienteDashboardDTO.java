package com.proyectoClinica.dto.response;
/*DTO Paciente en dashboard Medico*/
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PacienteDashboardDTO {
    private Long idPaciente;
    private String nombreCompleto;
    private Integer edad;
    private String genero;
    private String contactoEmail;
    private String contactoTelefonos;
    private String ultimaCita;
    private String diagnostico;
}

