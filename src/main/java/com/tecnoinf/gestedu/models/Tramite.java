package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.models.enums.EstadoTramite;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tramites")
public class Tramite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private EstadoTramite estado = EstadoTramite.NUEVO;
    private Date fecha;
    private TipoTramite tipo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_solicitante_id", nullable = false)
    private Usuario usuarioSolicitante;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_responsable_id", nullable = false)
    private Usuario usuarioResponsable;
}
