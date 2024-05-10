package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.specifications.CarreraSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CarreraServiceImpl(CarreraRepository carreraRepository, ModelMapper modelMapper) {
        this.carreraRepository = carreraRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<CreateCarreraDTO> getAllCarreras(Pageable pageable, String nombre) {
        CarreraSpecification spec = new CarreraSpecification(nombre);
        return carreraRepository.findAll(spec, pageable)
                .map(carrera -> modelMapper.map(carrera, CreateCarreraDTO.class));
    }

    @Override
    public CreateCarreraDTO getCarreraById(Long id) { //TEST AGREGUE COMENTARIOS
        return carreraRepository.findById(id)
                .map(carrera -> modelMapper.map(carrera, CreateCarreraDTO.class))
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
    public CreateCarreraDTO updateCarrera(Long id, CreateCarreraDTO createCarreraDto) {
        return carreraRepository.findById(id)
                .map(existingCarrera -> {
                    existingCarrera.setNombre(createCarreraDto.getNombre());
                    Carrera updatedCarrera = carreraRepository.save(existingCarrera);
                    return modelMapper.map(updatedCarrera, CreateCarreraDTO.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + id));
    }

    @Override
    public void deleteCarrera(Long id) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + id));
        carreraRepository.delete(carrera);
    }
}