package com.proyectoClinica.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    private String correo;

    @Column(nullable = false, length = 200)
    private String contrasena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(nullable = false, length = 20)
    private String telefono;

}
