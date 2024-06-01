package com.tecnoinf.gestedu.dtos.examen;

import com.tecnoinf.gestedu.dtos.DocenteDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActaExamenDTO {
    private Long id;
    private String fecha;
    private AsignaturaDTO asignatura;
    private List<DocenteDTO> docentes;
    private List<InscripcionExamenDTO> inscripciones;
}
