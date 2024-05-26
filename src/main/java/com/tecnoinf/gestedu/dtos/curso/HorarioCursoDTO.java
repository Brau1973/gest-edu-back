package com.tecnoinf.gestedu.dtos.curso;

import com.tecnoinf.gestedu.enumerados.DiaSemana;
import com.tecnoinf.gestedu.enumerados.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioCursoDTO {
    private Long idCurso;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer diasPrevInsc;
    private Estado estado;
    private Integer semestre;
    private Long asignaturaId;
    private Long idHorario;
    private DiaSemana dia;
    private Time horaInicio;
    private Time horaFin;
    private Long idDocente;
}
