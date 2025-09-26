package com.proyectoClinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_accion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaAccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "id_registro_afectado")
    private Integer idRegistroAfectado;

    @Column(nullable = false, length = 50)
    private String accion;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column
    private String detalle;

    @Column(name = "accion_tipo", length = 50)
    private String accionTipo;

}
