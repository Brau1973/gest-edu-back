package com.tecnoinf.gestedu.dtos.curso;

import com.tecnoinf.gestedu.enumerados.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDTO {
    private Long id;
    private DiaSemana dia;
    private Time horaInicio;
    private Time horaFin;
    private Set<Long> cursosId;

    public void addCursosId(Long cursoId) {
        if (this.cursosId == null) {
            this.cursosId = new HashSet<>();
        }
        this.cursosId.add(cursoId);
    }
}
