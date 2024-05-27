package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.examen.CreateExamenDTO;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;

public interface ExamenService {
    ExamenDTO altaExamen(CreateExamenDTO createExamenDto);
}
