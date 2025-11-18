package com.proyectoClinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "sub_especialidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubEspecialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subespecialidad")
    private Integer idSubespecialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_especialidad", nullable = false)
    private Especialidad especialidad;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column
    private String descripcion;

    @Column(name = "url_img")
    private String urlImg;

    @Column(name = "precio_subespecial")
    private BigDecimal precioSubespecial;
}
