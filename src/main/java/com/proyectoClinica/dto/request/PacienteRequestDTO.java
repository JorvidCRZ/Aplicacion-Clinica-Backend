package com.proyectoClinica.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteRequestDTO {
        @NotNull(message = "El idPersona es obligatorio")
        @Positive(message = "El idPersona debe ser positivo")
        private Integer idPersona;

        private String tipoSangre;
        private Double peso;
        private Double altura;
        private String contactoEmergenciaNombre;
        private String contactoEmergenciaRelacion;
        private String contactoEmergenciaTelefono;
       private PersonaRequestDTO persona;
       private UsuarioEditRequestDTO usuarioAgrego;
    }
