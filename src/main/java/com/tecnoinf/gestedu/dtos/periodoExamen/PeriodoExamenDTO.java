package com.tecnoinf.gestedu.dtos.periodoExamen;

import com.tecnoinf.gestedu.models.PeriodoExamen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoExamenDTO {
    private Long id;
    private String fechaInicio; //fecha 2024-05-30
    private String fechaFin;
    private Long carreraid;

    public PeriodoExamenDTO(PeriodoExamen periodoExamen) {
        this.id = periodoExamen.getId();
        this.fechaInicio = periodoExamen.getFechaInicio().toString();
        this.fechaFin = periodoExamen.getFechaFin().toString();
        this.carreraid = periodoExamen.getCarrera().getId();
    }
}
