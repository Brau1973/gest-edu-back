package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.models.enums.EstadoTramite;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tramites")
public class Tramite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaCreacion = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    private LocalDateTime fechaActualizacion = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    @Enumerated(EnumType.STRING)
    private TipoTramite tipo;
    @Enumerated(EnumType.STRING)
    private EstadoTramite estado = EstadoTramite.PENDIENTE;
    @Column(length = 500)
    private String motivoRechazo = "";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_solicitante_id", nullable = false)
    private Usuario usuarioSolicitante;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_responsable_id")
    private Usuario usuarioResponsable = null;
}
