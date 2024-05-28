package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.examen.CreateExamenDTO;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.CreateInscripcionExamenDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenDTO;

public interface ExamenService {
    ExamenDTO altaExamen(CreateExamenDTO createExamenDto);
    InscripcionExamenDTO inscribirseExamen(CreateInscripcionExamenDTO inscripcionExamenDto);
}
