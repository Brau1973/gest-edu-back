package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.inscripcionCarrera.InscripcionCarreraDTO;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.InscripcionCarrera;
import com.tecnoinf.gestedu.repositories.InscripcionCarreraRepository;
import com.tecnoinf.gestedu.services.interfaces.InscripcionCarreraService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscripcionCarreraServiceImpl  implements InscripcionCarreraService {

    private final InscripcionCarreraRepository inscripcionCarreraRepository;
    private final ModelMapper modelmapper;

    @Autowired
    public InscripcionCarreraServiceImpl(InscripcionCarreraRepository inscripcionCarreraRepository, ModelMapper modelmapper) {
        this.inscripcionCarreraRepository = inscripcionCarreraRepository;
        this.modelmapper = modelmapper;
    }

    @Override
    public InscripcionCarreraDTO createInscripcionCarrera(Carrera carrera, Estudiante estudiante) {
        InscripcionCarrera inscripcionCarrera = new InscripcionCarrera();
        inscripcionCarrera.setCarrera(carrera);
        inscripcionCarrera.setEstudiante(estudiante);
        return modelmapper.map(inscripcionCarreraRepository.save(inscripcionCarrera), InscripcionCarreraDTO.class);
    }
}
