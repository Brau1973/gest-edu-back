package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.inscripcionCarrera.InscripcionCarreraDTO;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.InscripcionCarrera;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.repositories.InscripcionCarreraRepository;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import com.tecnoinf.gestedu.services.interfaces.InscripcionCarreraService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscripcionCarreraServiceImpl  implements InscripcionCarreraService {

    private final InscripcionCarreraRepository inscripcionCarreraRepository;
    private final ModelMapper modelmapper;
    private final ActividadService actividadService;

    @Autowired
    public InscripcionCarreraServiceImpl(InscripcionCarreraRepository inscripcionCarreraRepository, ModelMapper modelmapper, ActividadService actividadService) {
        this.inscripcionCarreraRepository = inscripcionCarreraRepository;
        this.modelmapper = modelmapper;
        this.actividadService = actividadService;
    }

    @Override
    public InscripcionCarreraDTO createInscripcionCarrera(Carrera carrera, Estudiante estudiante) {
        InscripcionCarrera inscripcionCarrera = new InscripcionCarrera();
        inscripcionCarrera.setCarrera(carrera);
        inscripcionCarrera.setEstudiante(estudiante);

        // NO ES UNA ACTIVIDAD REALIZADA POR EL ESTUDIANTE, ES RESULTADO DE QUE EL FUNCIONARIO APRUEBE EL  TRAMITE
        //actividadService.registrarActividad(TipoActividad.INSCRIPCION_A_CARRERA, "Se ha inscrito a la carrera " + carrera.getNombre() + " con éxito");

        return modelmapper.map(inscripcionCarreraRepository.save(inscripcionCarrera), InscripcionCarreraDTO.class);
    }
}
