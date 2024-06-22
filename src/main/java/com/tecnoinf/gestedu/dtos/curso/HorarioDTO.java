package com.tecnoinf.gestedu.dtos.curso;

import java.time.LocalTime;
import com.tecnoinf.gestedu.models.enums.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDTO {
    private Long id;
    private DiaSemana dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    ////private Lon cursoId;
}
