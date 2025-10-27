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
public class RecordatorioResponseDTO {

    private Integer idRecordatorio;
    private CitaResponseDTO cita;
    private LocalDateTime fechaEnvio;
    private String tipo;
    private String estado;
    private String destinatarioCorreo;

}
