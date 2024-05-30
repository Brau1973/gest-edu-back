package com.tecnoinf.gestedu.dtos.curso;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.enums.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {
    //private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;
    private Integer diasPrevInsc;
    private Estado estado;
    //private Integer semestre;
    //private Set<HorarioDTO> horarios;
    private Long asignaturaId;
    private Long docenteId;

    public CursoDTO(Curso curso) {
        //this.id = curso.getId();
        this.fechaInicio = curso.getFechaInicio();
        this.fechaFin = curso.getFechaFin();
        this.diasPrevInsc = curso.getDiasPrevInsc();
        this.estado = curso.getEstado();
        //this.semestre = curso.getSemestre();
        this.asignaturaId = curso.getAsignatura().getId();
        this.docenteId = curso.getDocente().getId();
    }
}
