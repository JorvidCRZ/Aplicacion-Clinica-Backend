package com.proyectoClinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "cita_resumen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaResumen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita_resumen")
    private Integer idCitaResumen;

    // 🔗 Relación con Cita (FK id_cita)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita cita;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @Column(nullable = false, length = 50)
    private String estado;

    // 🔗 Relación con Medico (FK id_medico)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico")
    private Medico medico;

    @Column(length = 100)
    private String especialidad;

    @Column(length = 100)
    private String consultorio = "Por asignar";

    @Column(length = 100, nullable = false)
    private String tipo;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(columnDefinition = "TEXT")
    private String motivo;

    // ✅ Valor por defecto antes de insertar
    @PrePersist
    public void prePersist() {
        if (consultorio == null || consultorio.trim().isEmpty()) {
            consultorio = "Por asignar";
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            motivo = "Reserva vía checkout";
        }
    }
}
