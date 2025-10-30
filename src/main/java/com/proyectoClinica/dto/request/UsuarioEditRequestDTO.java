package com.proyectoClinica.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEditRequestDTO {
    @NotNull(message = "El id del usuario es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "El correo no puede ser nulo")
    @Email(message = "El formato del correo no es válido")
    private String correo;
}
