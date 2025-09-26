package com.proyectoClinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "recordatorio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recordatorio {

    @Id
    @GeneratedValue
    @Column(name = "id_recordatorio")
    private Integer idRecordatorio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita cita;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false, length = 20)
    private String estado;
}
