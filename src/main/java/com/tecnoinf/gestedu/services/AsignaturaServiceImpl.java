package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class AsignaturaServiceImpl implements AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;
    private final ModelMapper modelMapper;
    private final CarreraRepository carreraRepository;

    @Autowired
    public AsignaturaServiceImpl(AsignaturaRepository asignaturaRepository, CarreraRepository carreraRepository ,ModelMapper modelMapper) {
        this.asignaturaRepository = asignaturaRepository;
        this.carreraRepository = carreraRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AsignaturaDTO createAsignatura(CreateAsignaturaDTO createAsignaturaDto) {
        Carrera carrera = carreraRepository.findById(createAsignaturaDto.getCarreraId())
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + createAsignaturaDto.getCarreraId()));

        if (asignaturaRepository.existsByNombreAndCarreraId(createAsignaturaDto.getNombre(), carrera.getId())) {
            throw new UniqueFieldException("Ya existe una asignatura con el nombre " + createAsignaturaDto.getNombre() + " en la carrera  " + carrera.getNombre() + " (id Carrera: " + carrera.getId() + ")");
        }
        Asignatura asignatura = new Asignatura();
        asignatura = modelMapper.map(createAsignaturaDto, Asignatura.class);
        asignatura.setCarrera(carrera);
        asignatura.setId(null);
        Asignatura createdAsignatura = asignaturaRepository.save(asignatura);
        return modelMapper.map(createdAsignatura, AsignaturaDTO.class);
    }
}
