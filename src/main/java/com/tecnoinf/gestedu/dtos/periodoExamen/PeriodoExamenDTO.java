package com.tecnoinf.gestedu.dtos.periodoExamen;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tecnoinf.gestedu.models.PeriodoExamen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoExamenDTO {
    private Long id;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Long carreraid;

    public PeriodoExamenDTO(PeriodoExamen periodoExamen) {
        this.id = periodoExamen.getId();
        this.fechaInicio = periodoExamen.getFechaInicio();
        this.fechaFin = periodoExamen.getFechaFin();
        this.carreraid = periodoExamen.getCarrera().getId();
    }
}