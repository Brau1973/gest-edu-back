package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.specifications.CarreraSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;
    private final ModelMapper modelMapper;
    private final AsignaturaRepository asignaturaRepository;

    @Autowired
    public CarreraServiceImpl(CarreraRepository carreraRepository, ModelMapper modelMapper, AsignaturaRepository asignaturaRepository) {
        this.carreraRepository = carreraRepository;
        this.modelMapper = modelMapper;
        this.asignaturaRepository = asignaturaRepository;
    }

    @Override
    public Page<BasicInfoCarreraDTO> getAllCarreras(Pageable pageable, String nombre) {
        CarreraSpecification spec = new CarreraSpecification(nombre);
        return carreraRepository.findAll(spec, pageable)
                .map(carrera -> modelMapper.map(carrera, BasicInfoCarreraDTO.class));
    }

    @Override
    public BasicInfoCarreraDTO getCarreraBasicInfoById(Long id) {
        return carreraRepository.findById(id)
                .map(carrera -> modelMapper.map(carrera, BasicInfoCarreraDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + id));
    }

    @Override
    public CreateCarreraDTO createCarrera(CreateCarreraDTO createCarreraDto) {
        if (carreraRepository.existsByNombre(createCarreraDto.getNombre())) {
            throw new UniqueFieldException("Ya existe una carrera con el nombre " + createCarreraDto.getNombre());
        }
        Carrera carrera = modelMapper.map(createCarreraDto, Carrera.class);
        Carrera savedCarrera = carreraRepository.save(carrera);
        return modelMapper.map(savedCarrera, CreateCarreraDTO.class);
    }

    @Override
    public BasicInfoCarreraDTO updateCarrera(Long id, CreateCarreraDTO createCarreraDto) {
        return carreraRepository.findById(id)
                .map(existingCarrera -> {
                    if (createCarreraDto.getNombre() != null && !createCarreraDto.getNombre().isEmpty()) {
                        existingCarrera.setNombre(createCarreraDto.getNombre());
                    }
                    if (createCarreraDto.getDescripcion() != null && !createCarreraDto.getDescripcion().isEmpty()) {
                        existingCarrera.setDescripcion(createCarreraDto.getDescripcion());
                    }
                    Carrera updatedCarrera = carreraRepository.save(existingCarrera);
                    return modelMapper.map(updatedCarrera, BasicInfoCarreraDTO.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + id));
    }

    @Override
    public void deleteCarrera(Long id) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + id));
        carreraRepository.delete(carrera);
    }

    @Override
    public Page<BasicInfoCarreraDTO> getCarrerasSinPlanDeEstudio(Pageable pageable) {
        return carreraRepository.findByExistePlanEstudioFalse(pageable)
                .map(carrera -> modelMapper.map(carrera, BasicInfoCarreraDTO.class));
    }

    @Override
    public Page<AsignaturaDTO> getAsignaturasFromCarrera(Long id, Pageable pageable) {
        return asignaturaRepository.findAllByCarreraId(id, pageable)
                .map(asignatura -> modelMapper.map(asignatura, AsignaturaDTO.class));
    }

    @Override
    public void updateSemestrePlanEstudio(Long id, List<AsignaturaDTO> asignaturasDto) {
        for (AsignaturaDTO asignaturaDto : asignaturasDto) {
            Asignatura asignatura = asignaturaRepository.findById(asignaturaDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + asignaturaDto.getId()));
            asignatura.setSemestrePlanEstudio(asignaturaDto.getSemestrePlanEstudio());
            asignaturaRepository.save(asignatura);
        }
        carreraRepository.findById(id)
                .ifPresent(carrera -> {
                    carrera.setExistePlanEstudio(true);
                    carreraRepository.save(carrera);
                });
    }
}