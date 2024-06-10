package com.tecnoinf.gestedu;

import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.*;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCarrera;
import com.tecnoinf.gestedu.models.enums.EstadoTramite;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import com.tecnoinf.gestedu.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@SpringBootApplication
@EnableAsync
public class GestionEducativaOnlineApplication {
	;
	@Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

	public static void main(String[] args) {
        SpringApplication.run(GestionEducativaOnlineApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, CarreraRepository carreraRepository, AsignaturaRepository asignaturaRepository,
                                      InscripcionCarreraRepository inscripcionCarreraRepository, TramiteRepository tramiteRepository,
                                      DocenteRepository docenteRepository, CursoRepository cursoRepository, PeriodoExamenRepository periodoExamenRepository,
                                      ExamenRepository examenRepository, InscripcionCursoRepository inscripcionCursoRepository,
                                      InscripcionExamenRepository inscripcionExamenRepository, HorarioRepository horarioRepository) {

        return (args) -> {
            if (ddlAuto.equals("create") || ddlAuto.equals("create-drop")) {

				PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

				Usuario admin = new Administrador();

				Coordinador coordinador1 = new Coordinador();
				Coordinador coordinador2 = new Coordinador();

				Funcionario funcionario1 = new Funcionario();
				Funcionario funcionario2 = new Funcionario();

				Estudiante estudiante1 = new Estudiante();
                Estudiante estudiante2 = new Estudiante();
                Estudiante estudiante3 = new Estudiante();
				Estudiante estudiante4 = new Estudiante();
				Estudiante estudiante5 = new Estudiante();
				Estudiante estudiante6 = new Estudiante();
				Estudiante estudiante7 = new Estudiante();
				Estudiante estudiante8 = new Estudiante();
				Estudiante estudiante9 = new Estudiante();


                //---------------------------------------------------------------------------------------USUARIOS------------------------------------------------------------------------------------------------------------------------
                createAndSaveUser(usuarioRepository, admin, "Pedro", "Perez", "PedroPeAdmin@mail.com", "1234", "61647956", "091367159", "Lindoro Forteza 5548", passwordEncoder);
                createAndSaveUser(usuarioRepository, estudiante1, "Luis", "Diaz", "LuisDiaz@mail.com", "1234", "42569843", "093874635", "Elias Regules 7898", passwordEncoder);
				createAndSaveUser(usuarioRepository, estudiante2, "Horacio", "Hernandez", "HoriHernandez@mail.com", "1234", "61253594", "096558132", "Pitagoras 1122", passwordEncoder);
				createAndSaveUser(usuarioRepository, estudiante3, "Ana Maria", "Diaz", "AnaMariaDiaz@mail.com", "1234", "36984582", "095664889", "Cmno Carrasco 2551", passwordEncoder);
				createAndSaveUser(usuarioRepository, estudiante4, "Lucas", "Hernandez", "LucasHernandez@mail.com", "1234", "36714582", "095554889", "Missipi 2551", passwordEncoder);
				createAndSaveUser(usuarioRepository, estudiante5, "Serena", "Williams","SerenaWi@mail.com", "1234", "84714582", "095554871", "Gonzalo Ramirez 4795", passwordEncoder);
				createAndSaveUser(usuarioRepository, estudiante6, "Agustin", "Cannobio","AgusCanobbio@mail.com", "1234", "15714582", "095554571", "Zum felde 1475", passwordEncoder);
				createAndSaveUser(usuarioRepository, estudiante7, "Diego", "Forlan","DiegoForlan@mail.com", "1234", "84824582", "095144871", "Chacarita 1457", passwordEncoder);
				createAndSaveUser(usuarioRepository, estudiante8, "Nicolas", "Badaracco","NicoBada@mail.com", "1234", "19514582", "096374871", "Porongos 4795", passwordEncoder);
				createAndSaveUser(usuarioRepository, estudiante9, "Mauricio", "Mujica","MauriMu@mail.com", "1234", "48314582", "091654871", "Jose Cabrera 3335", passwordEncoder);
				createAndSaveUser(usuarioRepository, funcionario1, "Carla", "Miranda", "CarlaMiranda@mail.com", "1234", "42687516", "097895632", "Bv Artigas 3556", passwordEncoder);
				createAndSaveUser(usuarioRepository, funcionario2, "Andrew", "Garfield", "AndrewGarfield@mail.com", "1234", "42917516", "097145632", "Bv España 3556", passwordEncoder);
				createAndSaveUser(usuarioRepository, coordinador1, "Juan Pablo", "Salinas", "JuanPSalinas@mail.com", "1234", "31247689", "094658759", "Av Italia 6554", passwordEncoder);
				createAndSaveUser(usuarioRepository, coordinador2, "Fernando", "Dearrascaeta", "FerDearrasca@mail.com", "1234", "31177689", "094798759", "Ruta IB 6554", passwordEncoder);

                //-----------------------------------------------------------------------------------CARRERAS--------------------------------------------------------------------------------------------------------------------
				Carrera savedCarrera1 = createAndSaveCarrera(carreraRepository, "Tecnologo informatico", "Carrera de tecnologo informatico donde se enseña a programar en java, c++, c# y python", true);
				Carrera savedCarrera2 = createAndSaveCarrera(carreraRepository, "Diseño UX/UI", "Carrera de diseño UX/UI donde se enseña a diseñar interfaces de usuario y experiencia de usuario", false);
				Carrera savedCarrera3 = createAndSaveCarrera(carreraRepository, "Tecnologo Audiovisual", "Carrera de tecnologo audiovisual donde se enseña a editar videos y sonido", true);

                //----------------------------------------------------------------------------------ASIGNATURAS-------------------------------------------------------------------------------------------------------------
                Asignatura COE = createAsignaturaInitData(asignaturaRepository, "Comunicacion oral y Escrita", "Asignatura de comunicacion oral y escrita", 4, 3, savedCarrera1);
				Asignatura MDL1 = createAsignaturaInitData(asignaturaRepository, "Matematica discreta y logica 1", "Conjuntos y subconjuntos", 3, 2, savedCarrera1);
				Asignatura PAV = createAsignaturaInitData(asignaturaRepository, "Programacion avanzada", "OOP con java", 2, 1, savedCarrera1);

                createAsignaturaInitData(asignaturaRepository, "Diseño de Interfaz", "Asignatura de diseño de interfaz de usuario", 4, 0, savedCarrera2);
                createAsignaturaInitData(asignaturaRepository, "Experiencia de Usuario", "Asignatura de experiencia de usuario", 3, 0, savedCarrera2);
                createAsignaturaInitData(asignaturaRepository, "Prototipado", "Asignatura de prototipado de interfaces", 2, 0, savedCarrera2);

                createAsignaturaInitData(asignaturaRepository, "Grabacion con camara PRO", "Grabacion Full HD 4k", 2, 1, savedCarrera3);
                createAsignaturaInitData(asignaturaRepository, "Adobe PRO", "Edicion de videos", 8, 2, savedCarrera3);
                createAsignaturaInitData(asignaturaRepository, "Photoshop", "Photoshop con adobe ilustrator", 1, 3, savedCarrera3);

                // -------------------------------------------------------------------------------------TRAMITES---------------------------------------------------------------------------------------------------
                createTramiteInitData(tramiteRepository, estudiante1, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.PENDIENTE, null,"");
                createTramiteInitData(tramiteRepository, estudiante2, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.PENDIENTE, null,"");

				createTramiteInitData(tramiteRepository, estudiante3, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario1,"");
				createTramiteInitData(tramiteRepository, estudiante4, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario1,"");
				createTramiteInitData(tramiteRepository, estudiante5, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario1,"");
				createTramiteInitData(tramiteRepository, estudiante7, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario1,"");
				createTramiteInitData(tramiteRepository, estudiante8, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario1,"");
				createTramiteInitData(tramiteRepository, estudiante9, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario1,"");
				
				createTramiteInitData(tramiteRepository, estudiante6, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.RECHAZADO, funcionario1,"Faltante de documentacion");

				createTramiteInitData(tramiteRepository, estudiante4, savedCarrera1, TipoTramite.SOLICITUD_DE_TITULO, EstadoTramite.ACEPTADO, coordinador1,"");
				createTramiteInitData(tramiteRepository, estudiante5, savedCarrera1, TipoTramite.SOLICITUD_DE_TITULO, EstadoTramite.RECHAZADO, coordinador2,"No se tienen los creditos necesarios para solicitar el titulo");

                // ----------------------------------------------------------------------------------INSCRIPCIONCARRERA-----------------------------------------------------------------------------------------
				createAndSaveInscripcionCarrera(inscripcionCarreraRepository, estudiante3, savedCarrera1, EstadoInscripcionCarrera.CURSANDO ,LocalDate.now().minusMonths(5));
				createAndSaveInscripcionCarrera(inscripcionCarreraRepository, estudiante5, savedCarrera1, EstadoInscripcionCarrera.CURSANDO, LocalDate.now().minusMonths(10));
				createAndSaveInscripcionCarrera(inscripcionCarreraRepository, estudiante7, savedCarrera1, EstadoInscripcionCarrera.CURSANDO ,LocalDate.now().minusMonths(5));
				createAndSaveInscripcionCarrera(inscripcionCarreraRepository, estudiante4, savedCarrera1, EstadoInscripcionCarrera.COMPLETADA ,LocalDate.now().minusYears(2));
				createAndSaveInscripcionCarrera(inscripcionCarreraRepository, estudiante8, savedCarrera1, EstadoInscripcionCarrera.CURSANDO, LocalDate.now().minusMonths(11));
				createAndSaveInscripcionCarrera(inscripcionCarreraRepository, estudiante9, savedCarrera1, EstadoInscripcionCarrera.CURSANDO ,LocalDate.now().minusMonths(2));

                //---------------------------------------------------------------------------------------- DOCENTES ----------------------------------------------------------------------------------------
				Docente docente1 = createAndSaveDocente(docenteRepository, "Fernando", "Garcia", "42569897");
				Docente docente2 = createAndSaveDocente(docenteRepository, "Manuel", "Palomino", "42585843");
				Docente docente3 = createAndSaveDocente(docenteRepository, "Veronica", "Gonzalez", "42569734");

                //----------------------------------------------------------------------------------------- CURSOS -----------------------------------------------------------------------------------------
				//TECNOLOGO INFORMATICO

				//CURSOS FINALIZADOS (VIEJOS)
				Curso cursoCOEViejo = createAndSaveCurso(cursoRepository, Estado.FINALIZADO, COE, LocalDate.of(2023, 7, 15), LocalDate.of(2023, 11, 15), docente1, 30);
				Curso cursoMDL1Viejo = createAndSaveCurso(cursoRepository, Estado.FINALIZADO, MDL1, LocalDate.of(2023, 7, 15), LocalDate.of(2023, 11, 15), docente2, 30);
				Curso cursoPAVViejo = createAndSaveCurso(cursoRepository, Estado.FINALIZADO, PAV, LocalDate.of(2023, 7, 15), LocalDate.of(2023, 11, 15), docente3, 30);

				//CURSOS ACTIVOS (FECHAS DINAMICAS)
				Curso cursoCOE = createAndSaveCurso(cursoRepository, Estado.ACTIVO, COE, LocalDate.now().plusDays(20), LocalDate.now().plusDays(20).plusMonths(4), docente1, 30);
				Curso cursoMDL1 = createAndSaveCurso(cursoRepository, Estado.ACTIVO, MDL1, LocalDate.now().plusDays(20), LocalDate.now().plusDays(20).plusMonths(4), docente2, 30);
				Curso cursoPAV = createAndSaveCurso(cursoRepository, Estado.ACTIVO, PAV, LocalDate.now().plusDays(20), LocalDate.now().plusDays(20).plusMonths(4), docente3, 30);
				//HORARIOS
				createAndSaveHorario(horarioRepository,DiaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0), cursoCOE);
				createAndSaveHorario(horarioRepository,DiaSemana.MIERCOLES, LocalTime.of(10, 0), LocalTime.of(12, 0), cursoCOE);
				createAndSaveHorario(horarioRepository,DiaSemana.MARTES, LocalTime.of(9, 0), LocalTime.of(11, 0), cursoMDL1);
				createAndSaveHorario(horarioRepository,DiaSemana.JUEVES, LocalTime.of(11, 0), LocalTime.of(13, 0), cursoMDL1);
				createAndSaveHorario(horarioRepository,DiaSemana.MIERCOLES, LocalTime.of(8, 0), LocalTime.of(10, 0), cursoPAV);
				createAndSaveHorario(horarioRepository,DiaSemana.VIERNES, LocalTime.of(10, 0), LocalTime.of(12, 0), cursoPAV);

				//CURSOS FUTUROS (NO SE PUEDE INSCRIBIR AUN)
				Curso cursoCOEFuturo = createAndSaveCurso(cursoRepository, Estado.ACTIVO, COE, LocalDate.now().plusDays(40), LocalDate.now().plusDays(40).plusMonths(4), docente1, 30);
				Curso cursoMDL1Futuro = createAndSaveCurso(cursoRepository, Estado.ACTIVO, MDL1, LocalDate.now().plusDays(40), LocalDate.now().plusDays(40).plusMonths(4), docente2, 30);

				//------------------------------------------------------------------------------- INSCRIPCIONCURSO -------------------------------------------------------------------------------
				// INSCRIPCIONES CURSOS VIEJOS QUE EXONERO EL ESTUDIANTE 7 (TIENE TODOS LOS CREDITOS PARA SOLICITAR EL TITULO)
				InscripcionCurso inscripcionCurso1 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante7, cursoCOEViejo, EstadoInscripcionCurso.COMPLETADA, CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10) );
				InscripcionCurso inscripcionCurso2 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante7, cursoMDL1Viejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10));
				InscripcionCurso inscripcionCurso3 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante7, cursoPAVViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10));

				// INSCRIPCIONES A CURSO EN DISTINTOS ESTADOS
				InscripcionCurso inscripcionCurso4 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante5, cursoCOEViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.AEXAMEN,LocalDate.of(2023, 7, 10));
				InscripcionCurso inscripcionCurso5 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante5, cursoMDL1, EstadoInscripcionCurso.CURSANDO,CalificacionCurso.PENDIENTE, LocalDate.now());
				InscripcionCurso inscripcionCurso6 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante5, cursoPAVViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.AEXAMEN, LocalDate.of(2023, 7, 10));

				InscripcionCurso inscripcionCurso7 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante8, cursoCOEViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10));
				InscripcionCurso inscripcionCurso8 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante8, cursoMDL1Viejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.AEXAMEN, LocalDate.of(2023, 7, 10));
				InscripcionCurso inscripcionCurso9 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante8, cursoPAVViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.AEXAMEN, LocalDate.of(2023, 7, 10));

				InscripcionCurso inscripcionCurso10 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante9, cursoCOEViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.RECURSA, LocalDate.of(2023, 7, 10));
				InscripcionCurso inscripcionCurso11 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante9, cursoMDL1, EstadoInscripcionCurso.CURSANDO,CalificacionCurso.PENDIENTE, LocalDate.now());
				InscripcionCurso inscripcionCurso12 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante9, cursoPAVViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10));

				//ESTUDIANTE 3 NO ESTA INSCRIPTO A NINGUN CURSO

                //------------------------------------------------------------------------------- PERIODO EXAMEN ----------------------------------------------------------------------------------
				PeriodoExamen periodoExamen1 = createAndSavePeriodoExamen(periodoExamenRepository, carreraRepository, 1L, 5, 10);

                //------------------------------------------------------------------------------------- EXAMEN -------------------------------------------------------------------------------------
				// ACTIVOS
				Examen examenCOE = createAndSaveExamen(examenRepository, LocalDateTime.now().plusDays(7), 20, Estado.ACTIVO, COE, docente1);
				Examen examenPAV = createAndSaveExamen(examenRepository, LocalDateTime.now().plusDays(7), 20, Estado.ACTIVO, PAV, docente3);
				Examen examenMDL1 = createAndSaveExamen(examenRepository, LocalDateTime.now().plusDays(7), 20, Estado.ACTIVO, MDL1, docente2);

				// FINALIZADOS
				Examen examenMDL1Finalizado = createAndSaveExamen(examenRepository, LocalDateTime.now().minusMonths(5), 20, Estado.FINALIZADO, MDL1, docente2);
				Examen examenPAVFinalizado = createAndSaveExamen(examenRepository, LocalDateTime.now().minusMonths(5), 20, Estado.FINALIZADO, PAV, docente3);
				Examen examenCOEFinalizado = createAndSaveExamen(examenRepository, LocalDateTime.now().minusMonths(5), 20, Estado.FINALIZADO, COE, docente1);

                //----- INSCRIPCIONEXAMEN -----
				InscripcionExamen inscripcionExamen1 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante5, examenPAVFinalizado, CalificacionExamen.REPROBADO, LocalDateTime.now().minusMonths(5).minusDays(2));
				InscripcionExamen inscripcionExamen2 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante5, examenPAV, CalificacionExamen.PENDIENTE,LocalDateTime.now());
				InscripcionExamen inscripcionExamen7 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante5, examenCOEFinalizado, CalificacionExamen.APROBADO,LocalDateTime.now().minusMonths(5).minusDays(2));

				InscripcionExamen inscripcionExamen3 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante8, examenPAVFinalizado, CalificacionExamen.REPROBADO,LocalDateTime.now().minusMonths(5).minusDays(2));
				InscripcionExamen inscripcionExamen4 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante8, examenPAV, CalificacionExamen.PENDIENTE,LocalDateTime.now());
				InscripcionExamen inscripcionExamen5 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante8, examenMDL1Finalizado, CalificacionExamen.REPROBADO,LocalDateTime.now().minusMonths(5).minusDays(2));
				InscripcionExamen inscripcionExamen6 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante8, examenMDL1, CalificacionExamen.PENDIENTE,LocalDateTime.now());
            }
        };
    }

	private void createAndSaveUser(UsuarioRepository usuarioRepository, Usuario user, String nombre, String apellido, String email, String password, String ci, String telefono, String domicilio, PasswordEncoder passwordEncoder) {
		user.setNombre(nombre);
		user.setApellido(apellido);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setCi(ci);
		user.setTelefono(telefono);
		user.setDomicilio(domicilio);
		user.setIsEnable(true);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		usuarioRepository.save(user);
	}

	private Carrera createAndSaveCarrera(CarreraRepository carreraRepository, String nombre, String descripcion, boolean existePlanEstudio) {
    Carrera carrera = new Carrera();
    carrera.setNombre(nombre);
    carrera.setDescripcion(descripcion);
    carrera.setExistePlanEstudio(existePlanEstudio);
    return carreraRepository.save(carrera);
	}

	private Asignatura createAsignaturaInitData(AsignaturaRepository asignaturaRepository, String nombre, String descripcion, Integer creditos, Integer semestrePlanEstudio, Carrera carrera) {
		Asignatura asignatura = new Asignatura(null, nombre, descripcion, creditos, semestrePlanEstudio, carrera, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		return asignaturaRepository.save(asignatura);
	}

    private void createTramiteInitData(TramiteRepository tramiteRepository, Estudiante estudiante, Carrera savedCarrera, TipoTramite tipoTramite, EstadoTramite estadoTramite, Usuario usuarioResponsable, String motivoRechazo) {
        Tramite tramite = new Tramite();
        tramite.setUsuarioSolicitante(estudiante);
        tramite.setTipo(tipoTramite);
        tramite.setEstado(estadoTramite);
        tramite.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        tramite.setCarrera(savedCarrera);
        if (estadoTramite.equals(EstadoTramite.ACEPTADO) || estadoTramite.equals(EstadoTramite.RECHAZADO)) {
            tramite.setUsuarioResponsable(usuarioResponsable);
			if (estadoTramite.equals(EstadoTramite.RECHAZADO)){
				tramite.setMotivoRechazo(motivoRechazo);
			}
        }
        tramiteRepository.save(tramite);
    }

	private void createAndSaveInscripcionCarrera(InscripcionCarreraRepository inscripcionCarreraRepository, Estudiante estudiante, Carrera carrera, EstadoInscripcionCarrera estado, LocalDate fechaInscripcion) {
		InscripcionCarrera inscripcionCarrera = new InscripcionCarrera();
		inscripcionCarrera.setEstudiante(estudiante);
		inscripcionCarrera.setCarrera(carrera);
		inscripcionCarrera.setEstado(estado);
		inscripcionCarrera.setFechaInscripcion(fechaInscripcion);
		inscripcionCarreraRepository.save(inscripcionCarrera);
	}

	private Docente createAndSaveDocente(DocenteRepository docenteRepository, String nombre, String apellido, String documento) {
		Docente docente = new Docente();
		docente.setNombre(nombre);
		docente.setApellido(apellido);
		docente.setDocumento(documento);
		return docenteRepository.save(docente);
	}

	private Curso createAndSaveCurso(CursoRepository cursoRepository, Estado estado, Asignatura asignatura, LocalDate fechaInicio, LocalDate fechaFin, Docente docente, int diasPrevInsc) {
		Curso curso = new Curso();
		curso.setEstado(estado);
		curso.setAsignatura(asignatura);
		curso.setFechaInicio(fechaInicio);
		curso.setFechaFin(fechaFin);
		curso.setDocente(docente);
		curso.setDiasPrevInsc(diasPrevInsc);
		return cursoRepository.save(curso);
	}

	private InscripcionCurso createAndSaveInscripcionCurso(InscripcionCursoRepository inscripcionCursoRepository, Estudiante estudiante, Curso curso, EstadoInscripcionCurso estadoInscripcionCurso,CalificacionCurso calificacion, LocalDate fechaInscripcion) {
		InscripcionCurso inscripcionCurso = new InscripcionCurso();
		inscripcionCurso.setEstudiante(estudiante);
		inscripcionCurso.setCurso(curso);
		inscripcionCurso.setEstado(estadoInscripcionCurso);
		inscripcionCurso.setCalificacion(calificacion);
		inscripcionCurso.setFechaInscripcion(fechaInscripcion);
		return inscripcionCursoRepository.save(inscripcionCurso);
	}

	private PeriodoExamen createAndSavePeriodoExamen(PeriodoExamenRepository periodoExamenRepository, CarreraRepository carreraRepository, Long carreraId, int startDays, int endDays) {
		PeriodoExamen periodoExamen = new PeriodoExamen();
		periodoExamen.setFechaInicio(LocalDateTime.now().plusDays(startDays));
		periodoExamen.setFechaFin(LocalDateTime.now().plusDays(endDays));
		periodoExamen.setCarrera(carreraRepository.findById(carreraId).get());
		return periodoExamenRepository.save(periodoExamen);
	}

	private Examen createAndSaveExamen(ExamenRepository examenRepository, LocalDateTime fecha, int diasPrevInsc, Estado estado, Asignatura asignatura, Docente docente) {
		Examen examen = new Examen();
		examen.setFecha(fecha);
		examen.setDiasPrevInsc(diasPrevInsc);
		examen.setEstado(estado);
		examen.setAsignatura(asignatura);
		examen.setDocentes(new ArrayList<>());
		examen.getDocentes().add(docente);
		return examenRepository.save(examen);
	}

	private InscripcionExamen createAndSaveInscripcionExamen(InscripcionExamenRepository inscripcionExamenRepository, Estudiante estudiante, Examen examen, CalificacionExamen calificacion, LocalDateTime fechaInscripcion) {
		InscripcionExamen inscripcionExamen = new InscripcionExamen();
		inscripcionExamen.setEstudiante(estudiante);
		inscripcionExamen.setExamen(examen);
		inscripcionExamen.setCalificacion(calificacion);
		inscripcionExamen.setFechaInscripcion(fechaInscripcion);
		return inscripcionExamenRepository.save(inscripcionExamen);
	}

	private void createAndSaveHorario(HorarioRepository horarioRepository , DiaSemana dia, LocalTime horaInicio, LocalTime horaFin, Curso curso) {
		Horario horario = new Horario();
		horario.setDia(dia);
		horario.setHoraInicio(horaInicio);
		horario.setHoraFin(horaFin);
		horario.setCurso(curso);
		horarioRepository.save(horario);
	}

	private Actividad createAndSaveActividad(ActividadRepository actividadRepository, TipoActividad tipoActividad, String descripcion) {
		Actividad actividad = new Actividad();
		actividad.setTipoActividad(tipoActividad);
		actividad.setDescripcion(descripcion);
		return actividadRepository.save(actividad);
	}

}
