package com.tecnoinf.gestedu.dtos.curso;

import com.tecnoinf.gestedu.enumerados.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDTO {
    private Long id;
    private DiaSemana dia;
    private Time horaInicio;
    private Time horaFin;
}
