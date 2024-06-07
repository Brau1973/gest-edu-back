package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.models.enums.TipoActividad;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="actividades")
public class Actividad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    @Enumerated(EnumType.STRING)
    private TipoActividad tipoActividad;
    private String descripcion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;


}