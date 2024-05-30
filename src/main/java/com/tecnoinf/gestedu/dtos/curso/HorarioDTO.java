package com.tecnoinf.gestedu.dtos.curso;

import com.tecnoinf.gestedu.models.enums.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDTO {
    //private Long id;
    private DiaSemana dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Long cursoId;
}
