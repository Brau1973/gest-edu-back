package com.tecnoinf.gestedu.dtos.curso;

import java.time.LocalDate;
import java.util.List;

import com.tecnoinf.gestedu.dtos.DocenteDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoEstudianteDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActaCursoDTO {
    private Long id;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    private AsignaturaDTO asignatura;
    private CursoDTO curso;
    private DocenteDTO docente;
    private List<InscripcionCursoDTO> inscripciones;
    private List<BasicInfoEstudianteDTO> estudiantes;

}
