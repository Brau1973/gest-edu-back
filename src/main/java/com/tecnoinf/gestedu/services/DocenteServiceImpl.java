package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.DocenteDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Docente;
import com.tecnoinf.gestedu.repositories.DocenteRepository;
import com.tecnoinf.gestedu.repositories.specifications.DocenteSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DocenteServiceImpl implements DocenteService{

    private final DocenteRepository docenteRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DocenteServiceImpl(DocenteRepository docenteRepository, ModelMapper modelMapper) {
        this.docenteRepository = docenteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<DocenteDTO> getAllDocentes(Pageable pageable,  String documento, String nombre, String apellido) {
        DocenteSpecification spec = new DocenteSpecification(documento, nombre, apellido);
        return docenteRepository.findAll(spec, pageable)
                .map(docente -> modelMapper.map(docente, DocenteDTO.class));
    }

    @Override
    public DocenteDTO getDocenteById(Long id) {
        Docente docente = findDocenteById(id);
        return modelMapper.map(docente, DocenteDTO.class);
    }

    @Override
    public DocenteDTO createDocente(DocenteDTO docenteDto) {
        checkDocumentoExists(docenteDto.getDocumento());
        Docente docente = modelMapper.map(docenteDto, Docente.class);
        Docente savedDocente = docenteRepository.save(docente);
        return modelMapper.map(savedDocente, DocenteDTO.class);
    }

    @Override
    public DocenteDTO updateDocente(Long id, DocenteDTO docenteDto) {
        checkDocumentoExists(docenteDto.getDocumento());
        Docente existingDocente = findDocenteById(id);
        existingDocente.setDocumento(docenteDto.getDocumento());
        existingDocente.setNombre(docenteDto.getNombre());
        existingDocente.setApellido(docenteDto.getApellido());
        Docente updatedDocente = docenteRepository.save(existingDocente);
        return modelMapper.map(updatedDocente, DocenteDTO.class);
    }

    @Override
    public void deleteDocente(Long id) { //TODO: CONTROLAR QUE NO ESTE ASIGNADO EN NINGUNA ASIGNATURA NI NINGUNA MESA DE EXAMEN
        Docente docente = findDocenteById(id);
        docenteRepository.delete(docente);
    }

    private Docente findDocenteById(Long id) {
        return docenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro un docente con el id " + id));
    }

    private void checkDocumentoExists(String documento) {
        if (docenteRepository.existsByDocumento(documento)) {
            throw new UniqueFieldException("Ya existe un docente con el documento " + documento);
        }
    }
}
