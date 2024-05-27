package com.tecnoinf.gestedu.dtos.curso;

import com.tecnoinf.gestedu.enumerados.CalificacionCursada;
import com.tecnoinf.gestedu.enumerados.EstadoInscACurso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionACursoDTO {
    private Long id;
    private EstadoInscACurso estadoInscACurso;
    private CalificacionCursada calificacion;
    private Date fechaInscripcion;
    private Long idEstudiante;
    private Long idCurso;
}
