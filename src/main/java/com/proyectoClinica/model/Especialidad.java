package com.proyectoClinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "especialidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Integer idEspecialidad;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column
    private String descripcion;

    @Column
    private BigDecimal precio;
}
