package com.tecnoinf.gestedu.repository;

import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCarrera;
import com.tecnoinf.gestedu.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class InscripcionCursoRepositoryTest {
    @Autowired
    private InscripcionCursoRepository inscripcionCursoRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscripcionCarreraRepository inscripcionCarreraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void findByEstudianteIdAndCursoAsignaturaIdReturnsExpectedData(){
        Long estudianteId; Long asignaturaId;
    }

    @Test
    public void findInscripcionCursoEstudianteByIdReturnsExpectedData(){
        Long estudianteId;
        //Primero necesito crear una inscropcion a curso, pero para eso tambien debo vrear las otras entidades
        //Crear Estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setCi("12345678");
        estudiante.setNombre("John");
        estudiante.setApellido("Doe");
        estudiante.setEmail("estudiante@gmail.com");
        estudiante.setPassword("12345678");
        estudiante = usuarioRepository.save(estudiante);

        // Crear carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        //InscripcionCarrera
        InscripcionCarrera inscCarrera = new InscripcionCarrera();
        inscCarrera.setCarrera(carrera);
        inscCarrera.setEstado(EstadoInscripcionCarrera.CURSANDO);
        inscCarrera.setFechaInscripcion((LocalDate.of(2025, 7, 15)));
        inscCarrera.setCreditosObtenidos(15);
        inscCarrera.setEstudiante(estudiante);
        inscCarrera = inscripcionCarreraRepository.save(inscCarrera);
        List<InscripcionCarrera> insCarreras = new ArrayList<>();
        insCarreras.add(inscCarrera);
        carrera.setInscripciones(insCarreras);
        carrera = carreraRepository.save(carrera);

        // Crear asignatura
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Asignatura Test 1");
        asignatura.setCarrera(carrera);
        asignatura = asignaturaRepository.save(asignatura);
        List<Asignatura> asignaturas = new ArrayList<>();
        asignaturas.add(asignatura);
        carrera.setAsignaturas(asignaturas);
        carrera = carreraRepository.save(carrera);

        //Crear Curso
        Curso curso = new Curso();
        LocalDate fechaInicio = LocalDate.of(2025, 3, 15); // Año, Mes (1-12), Día
        curso.setFechaInicio(fechaInicio);
        LocalDate fechaFin = LocalDate.of(2025, 7, 15); // Año, Mes (1-12), Día
        curso.setFechaFin(fechaFin);
        curso.setAsignatura(asignatura);
        curso.setEstado(Estado.ACTIVO);
        curso.setDiasPrevInsc(30);
        curso = cursoRepository.save(curso);

        //Crear Inscripcion Curso
        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setEstudiante(estudiante);
        inscripcionCurso = inscripcionCursoRepository.save(inscripcionCurso);

        //Llamo al metodo que se está probando
        List<InscripcionCurso> result = inscripcionCursoRepository.findInscripcionCursoEstudianteById(estudiante.getId());
        InscripcionCurso resultado = new InscripcionCurso();
        for(InscripcionCurso ins: result){
            resultado = ins;
        }

        // Verificar que el resultado es el esperado
        assertNotNull(result);
        assertEquals(resultado, inscripcionCurso);
    }

    @Test
    public void testFindByEstudianteIdAndCursoIdReturnsExpectedData() {
        //Crear Estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setCi("12345678");
        estudiante.setNombre("John");
        estudiante.setApellido("Doe");
        estudiante.setEmail("estudiante@gmail.com");
        estudiante.setPassword("12345678");
        estudiante = usuarioRepository.save(estudiante);

        // Crear carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        //InscripcionCarrera
        InscripcionCarrera inscCarrera = new InscripcionCarrera();
        inscCarrera.setCarrera(carrera);
        inscCarrera.setEstado(EstadoInscripcionCarrera.CURSANDO);
        inscCarrera.setFechaInscripcion((LocalDate.of(2025, 7, 15)));
        inscCarrera.setCreditosObtenidos(15);
        inscCarrera.setEstudiante(estudiante);
        inscCarrera = inscripcionCarreraRepository.save(inscCarrera);
        List<InscripcionCarrera> insCarreras = new ArrayList<>();
        insCarreras.add(inscCarrera);
        carrera.setInscripciones(insCarreras);
        carrera = carreraRepository.save(carrera);

        // Crear asignatura
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Asignatura Test 1");
        asignatura.setCarrera(carrera);
        asignatura = asignaturaRepository.save(asignatura);
        List<Asignatura> asignaturas = new ArrayList<>();
        asignaturas.add(asignatura);
        carrera.setAsignaturas(asignaturas);
        carrera = carreraRepository.save(carrera);

        //Crear Curso
        Curso curso = new Curso();
        LocalDate fechaInicio = LocalDate.of(2025, 3, 15); // Año, Mes (1-12), Día
        curso.setFechaInicio(fechaInicio);
        LocalDate fechaFin = LocalDate.of(2025, 7, 15); // Año, Mes (1-12), Día
        curso.setFechaFin(fechaFin);
        curso.setAsignatura(asignatura);
        curso.setEstado(Estado.ACTIVO);
        curso.setDiasPrevInsc(30);
        //curso = cursoRepository.save(curso);

        //Crear Inscripcion Curso
        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        inscripcionCurso.setFechaInscripcion(LocalDateTime.now());
        inscripcionCurso.setEstudiante(estudiante);
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setCalificacion(CalificacionCurso.PENDIENTE);
        inscripcionCurso = inscripcionCursoRepository.save(inscripcionCurso);

        curso.getInscripciones().add(inscripcionCurso);
        curso = cursoRepository.save(curso);

        // Ejecutar el método de prueba
        InscripcionCurso resultados = inscripcionCursoRepository.findInscripcionCursoEstudianteByEstudianteIdAndCursoId(estudiante.getId(), curso.getId());

        // Verificar los resultados
        assertNotNull(resultados);
        assertEquals(inscripcionCurso.getId(), resultados.getId());
    }
}
