package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InscripcionCurso {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CalificacionCurso calificacion = CalificacionCurso.PENDIENTE;
}
