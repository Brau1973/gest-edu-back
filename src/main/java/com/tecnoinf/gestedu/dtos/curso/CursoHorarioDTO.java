package com.tecnoinf.gestedu.dtos.curso;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tecnoinf.gestedu.models.enums.Estado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoHorarioDTO {
    private Long cursoId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;
    private Integer diasPrevInsc;
    private Estado estado;
    private String asignaturaNombre;
    private String docenteNombre;
    private String docenteApellido;
    //private List<CursoDTO> cursos;
    private List<HorarioDTO> horarios;
}
