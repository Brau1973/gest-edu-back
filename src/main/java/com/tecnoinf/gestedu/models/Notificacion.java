package com.tecnoinf.gestedu.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notificacion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private boolean leido;
    private String token;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Estudiante estudiante;

    public Notificacion(LocalDate fecha, boolean leido, Estudiante estudiante) {
        this.fecha = fecha;
        this.leido = leido;
        this.estudiante = estudiante;
    }
}