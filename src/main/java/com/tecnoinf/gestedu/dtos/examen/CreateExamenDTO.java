package com.tecnoinf.gestedu.dtos.examen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExamenDTO {
    private String fecha; //fecha y hora 2024-05-30T19:00
    private Integer diasPrevInsc;
    private Long asignaturaId;
    private Long[] docenteIds;
}
