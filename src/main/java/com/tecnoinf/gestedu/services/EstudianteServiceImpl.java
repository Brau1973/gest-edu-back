package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstudianteServiceImpl implements EstudianteService{

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;

    @Autowired
    public EstudianteServiceImpl(EstudianteRepository estudianteRepository ,CarreraRepository carreraRepository) {
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
    }

    @Override
    public Page<Carrera> getCarrerasNoInscripto(String email, Pageable pageable) {
        Optional<Usuario> estudiante = estudianteRepository.findByEmail(email);
        if (estudiante.isEmpty()) {
            throw new ResourceNotFoundException("Estudiante con email " + email + " no encontrado");
        }
        return carreraRepository.findCarrerasWithPlanEstudioAndEstudianteNotInscripto(estudiante.get().getId(), pageable);
    }
}