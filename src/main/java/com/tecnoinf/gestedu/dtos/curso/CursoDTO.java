package com.tecnoinf.gestedu.dtos.curso;

import com.tecnoinf.gestedu.enumerados.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {
    private Long id;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer diasPrevInsc;
    private Estado estado;
    private Integer semestre;
    private Set<HorarioDTO> horariosId;
    private Long asignaturaId;
}
