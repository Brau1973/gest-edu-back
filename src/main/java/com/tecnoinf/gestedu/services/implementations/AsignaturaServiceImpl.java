package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.exceptions.*;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.services.interfaces.AsignaturaService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<AsignaturaDTO> getPrevias(Long asignaturaId){
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + asignaturaId));
        List<Asignatura> previas = asignatura.getPrevias();
        Type listType = new TypeToken<List<AsignaturaDTO>>(){}.getType();
        return modelMapper.map(previas, listType);
    }

    @Override
    public AsignaturaDTO addPrevia(Long asignaturaId, Long previaId) {

        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura id " + asignaturaId + "no se encuentra"));
        Asignatura previa = asignaturaRepository.findById(previaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura id " + previaId + "no se encuentra"));

        if(!asignatura.getCarrera().getExistePlanEstudio()){
            throw new PlanEstudioNoExisteException("Primero debe ingresar un plan de estudio en la carrera.");
        }
        if(asignatura.getSemestrePlanEstudio()==0 || asignatura.getSemestrePlanEstudio()==1){
            throw new SemestreException("No se puede agregar una asignatura previa a una asignatura del primer semestre");
        }
        if(asignatura.getSemestrePlanEstudio() <= previa.getSemestrePlanEstudio()){
            throw new SemestreException("No se puede agregar una previa del mismo semestre o de un semestre mayor.");
        }
        if(asignatura.getPrevias().contains(previa)){
            throw new AsignaturaPreviaExistenteException("La asignatura previa ya se encuentra registrada");
        }
        if(existeCiclo(asignatura, previa)){
            throw new CicloEnAsignaturasException("No se puede agregar la asignatura previa porque se forma un ciclo");
        }
        asignatura.getPrevias().add(previa);
        Asignatura updatedAsignatura = asignaturaRepository.save(asignatura);
        return modelMapper.map(updatedAsignatura, AsignaturaDTO.class);
    }

    private boolean existeCiclo(Asignatura asignatura, Asignatura asignaturaPrevia) {
        if(asignaturaPrevia.equals(asignatura)) {
            return true;
        }
        List<Asignatura> previas = asignaturaPrevia.getPrevias();
        if(previas == null) {
            return false;
        }
        for(Asignatura previa : previas) {
            if(existeCiclo(asignatura, previa)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AsignaturaDTO> getNoPrevias(Long asignaturaId){
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + asignaturaId));

        List<Asignatura> asignaturasCarrera = asignaturaRepository.findByCarreraId(asignatura.getCarrera().getId());
        List<Asignatura> previas = asignatura.getPrevias();

        List<Asignatura> noPrevias = new ArrayList<>();
        for(Asignatura a : asignaturasCarrera){
            if(!previas.contains(a) && !a.equals(asignatura)){
                noPrevias.add(a);
            }
        }
        Type listType = new TypeToken<List<AsignaturaDTO>>(){}.getType();
        return modelMapper.map(noPrevias, listType);
    }

    @Override
    public AsignaturaDTO getAsignaturaById(Long id) {
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + id));
        return modelMapper.map(asignatura, AsignaturaDTO.class);
    }
}
