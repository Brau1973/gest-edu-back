package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Titulo;
import com.tecnoinf.gestedu.repositories.TituloRepository;
import com.tecnoinf.gestedu.services.interfaces.TituloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TituloServiceImpl implements TituloService {

    private final TituloRepository tituloRepository;

    @Autowired
    public TituloServiceImpl(TituloRepository tituloRepository) {
        this.tituloRepository = tituloRepository;
    }

    @Override
    public void createTitulo(String nombreCarrera, Estudiante estudiante) {
        Titulo titulo = new Titulo();
        titulo.setNombreCarrera(nombreCarrera);
        titulo.setEstudiante(estudiante);
        tituloRepository.save(titulo);
    }
}
