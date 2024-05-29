package com.tecnoinf.gestedu.dtos.inscripcionExamen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInscripcionExamenDTO {
    private String email;
    private Long examenId;
}
