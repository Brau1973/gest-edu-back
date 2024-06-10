package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCarrera.InscripcionCarreraDTO;
import com.tecnoinf.gestedu.dtos.periodoExamen.PeriodoExamenDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.ExamenRepository;
import com.tecnoinf.gestedu.repositories.PeriodoExamenRepository;
import com.tecnoinf.gestedu.repositories.specifications.CarreraSpecification;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import com.tecnoinf.gestedu.services.interfaces.CarreraService;
import com.tecnoinf.gestedu.services.interfaces.ExamenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;
    private final ModelMapper modelMapper;
    private final AsignaturaRepository asignaturaRepository;
    private final PeriodoExamenRepository periodoExamenRepository;
    private final ActividadService actividadService;
    private final ExamenRepository examenRepository;

    @Autowired
    public CarreraServiceImpl(CarreraRepository carreraRepository, ModelMapper modelMapper, AsignaturaRepository asignaturaRepository,
                              PeriodoExamenRepository periodoExamenRepository, ActividadService actividadService, ExamenRepository examenRepository) {
        this.carreraRepository = carreraRepository;
        this.modelMapper = modelMapper;
        this.asignaturaRepository = asignaturaRepository;
        this.periodoExamenRepository = periodoExamenRepository;
        this.actividadService = actividadService;
        this.examenRepository = examenRepository;
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

        actividadService.registrarActividad(TipoActividad.ALTA_CARRERA, "Se ha creado la carrera " + savedCarrera.getNombre());

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

                    actividadService.registrarActividad(TipoActividad.EDITAR_CARRERA, "Se ha editado la carrera con id " + updatedCarrera.getId());

                    return modelMapper.map(updatedCarrera, BasicInfoCarreraDTO.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + id));
    }

    @Override
    public void deleteCarrera(Long id) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + id));
        carreraRepository.delete(carrera);
        actividadService.registrarActividad(TipoActividad.BORRAR_CARRERA, "Se ha borrado  la carrera "+ carrera.getNombre());
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

        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + id));
        carrera.setExistePlanEstudio(true);
        carreraRepository.save(carrera);

        actividadService.registrarActividad(TipoActividad.REGISTRO_PLAN_DE_ESTUDIO, "Se ha registrado un plan de estudio para la carrera " + carrera.getNombre() + " (Id " + carrera.getId()+")");
    }

    @Override
    public List<InscripcionCarreraDTO> getEstudiantesInscriptos(Long id) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + id));
        return carrera.getInscripciones()
                .stream()
                .map(inscripcion -> modelMapper.map(inscripcion, InscripcionCarreraDTO.class))
                .toList();
    }

    @Override
    public Page<PeriodoExamenDTO> obtenerPeriodosExamenCarrera(Long id, Pageable pageable){
        List<PeriodoExamenDTO> periodosExamenDTO = new ArrayList<>();
        List<PeriodoExamen> periodosExamen = periodoExamenRepository.findAllByCarreraIdAndFechaFinAfter(id, LocalDate.now(), pageable).stream().toList();
        for (PeriodoExamen periodoExamen : periodosExamen) {
            PeriodoExamenDTO periodoExamenDTO = new PeriodoExamenDTO(periodoExamen);
            periodosExamenDTO.add(periodoExamenDTO);
        }
        return new PageImpl<>(periodosExamenDTO, pageable, periodosExamenDTO.size());
    }

    @Override
    public List<AsignaturaDTO> obtenerAsignaturasConExamenesActivos(Long id){
        List<AsignaturaDTO> asignaturasDTO = new ArrayList<>();
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + id));
        List<Asignatura> asignaturas = carrera.getAsignaturas();
        for (Asignatura asignatura : asignaturas) {
            Page<Examen> examenes = examenRepository.findAllByFechaBeforeAndEstadoAndAsignaturaId(LocalDateTime.now(), Estado.ACTIVO, asignatura.getId(), Pageable.unpaged());
            if(examenes.getTotalElements() > 0){
                AsignaturaDTO asignaturaDTO = modelMapper.map(asignatura, AsignaturaDTO.class);
                asignaturasDTO.add(asignaturaDTO);
            }
        }
        if(asignaturasDTO.isEmpty()){
            throw new ResourceNotFoundException("No hay asignaturas con examenes activos en la carrera con id " + id);
        }
        return asignaturasDTO;
    }

    @Override
    public List<CursoDTO>obtenerCursosActivos(Long idCarrera){
        Carrera carrera = carreraRepository.findById(idCarrera)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada"));

        // Crear la lista de cursos
        List<CursoDTO> cursosDeCarrera = new ArrayList<>();

        // Recorrer todas las asignaturas de la carrera
        for (Asignatura asignatura : carrera.getAsignaturas()) {
            // Recorrer todos los cursos de cada asignatura
            for (Curso curso : asignatura.getCursos()) {
                // Verificar que el curso esté finalizado
                    if(curso.getEstado().equals(Estado.ACTIVO)){
                        CursoDTO cursoDTO = modelMapper.map(curso, CursoDTO.class);
                        cursosDeCarrera.add(cursoDTO);
                    }
                }
            }
        return cursosDeCarrera;
    }
}