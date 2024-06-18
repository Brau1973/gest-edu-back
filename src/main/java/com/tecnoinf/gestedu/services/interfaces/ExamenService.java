package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.examen.ActaExamenDTO;
import com.tecnoinf.gestedu.dtos.examen.CreateExamenDTO;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenCalificacionDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.CreateInscripcionExamenDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenDTO;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExamenService {
    ExamenDTO altaExamen(CreateExamenDTO createExamenDto);
    InscripcionExamenDTO inscribirseExamen(CreateInscripcionExamenDTO inscripcionExamenDto);
    List<InscripcionExamenDTO> listarInscriptosExamen(Long id);
    Page<ExamenDTO> listarExamenesPendientesCalificar(Pageable pageable);
    List<InscripcionExamenCalificacionDTO> obtenerCalificaciones(Long id);
    List<InscripcionExamenCalificacionDTO> registrarCalificaciones(Long id, List<InscripcionExamenCalificacionDTO> calificaciones) throws MessagingException;
    InscripcionExamenDTO darseDeBajaExamen(Long id, String name);
    ActaExamenDTO generarActaExamen(Long id);
}
