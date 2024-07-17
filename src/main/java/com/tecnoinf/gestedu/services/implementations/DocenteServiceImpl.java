package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.DocenteDTO;
import com.tecnoinf.gestedu.exceptions.BajaDocenteException;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Docente;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.repositories.CursoRepository;
import com.tecnoinf.gestedu.repositories.DocenteRepository;
import com.tecnoinf.gestedu.repositories.ExamenRepository;
import com.tecnoinf.gestedu.repositories.specifications.DocenteSpecification;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import com.tecnoinf.gestedu.services.interfaces.DocenteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;
    private final ModelMapper modelMapper;
    private final ActividadService actividadService;
    private final CursoRepository cursoRepository;
    private final ExamenRepository examenRepository;

    @Autowired
    public DocenteServiceImpl(DocenteRepository docenteRepository, ModelMapper modelMapper, ActividadService actividadService, CursoRepository cursoRepository, ExamenRepository examenRepository) {
        this.docenteRepository = docenteRepository;
        this.modelMapper = modelMapper;
        this.actividadService = actividadService;
        this.cursoRepository = cursoRepository;
        this.examenRepository = examenRepository;
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

        actividadService.registrarActividad(TipoActividad.ALTA_DOCENTE, "Se ha creado el docente con nombre" + savedDocente.getNombre());

        return modelMapper.map(savedDocente, DocenteDTO.class);
    }

    @Override
    public DocenteDTO updateDocente(Long id, DocenteDTO docenteDto) {
        //checkDocumentoExists(docenteDto.getDocumento());
        Docente existingDocente = findDocenteById(id);
        //existingDocente.setDocumento(docenteDto.getDocumento());
        existingDocente.setNombre(docenteDto.getNombre());
        existingDocente.setApellido(docenteDto.getApellido());
        Docente updatedDocente = docenteRepository.save(existingDocente);

        actividadService.registrarActividad(TipoActividad.EDITAR_DOCENTE, "Se ha editado el docente con id" + updatedDocente.getId());

        return modelMapper.map(updatedDocente, DocenteDTO.class);
    }

    @Override
    public void deleteDocente(Long id) {
        if (cursoRepository.existsByDocenteId(id) || examenRepository.existsByDocentesId(id)) {
            throw new BajaDocenteException("El docente no puede ser eliminado porque está asignado a un curso o examen");
        }
        Docente docente = findDocenteById(id);
        docenteRepository.delete(docente);
        actividadService.registrarActividad(TipoActividad.BAJA_DOCENTE, "Se ha eliminado el docente con id" + id);
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
