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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

@SpringBootApplication
@EnableAsync
public class GestionEducativaOnlineApplication {

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String ddlAuto;

	public static void main(String[] args) {
		SpringApplication.run(GestionEducativaOnlineApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UsuarioRepository usuarioRepository, CarreraRepository carreraRepository, AsignaturaRepository asignaturaRepository,
									  InscripcionCarreraRepository inscripcionCarreraRepository, TramiteRepository tramiteRepository,
									  DocenteRepository docenteRepository,  CursoRepository cursoRepository, PeriodoExamenRepository periodoExamenRepository ,
									  ExamenRepository examenRepository, InscripcionCursoRepository inscripcionCursoRepository,
									  InscripcionExamenRepository inscripcionExamenRepository) {

		return (args) -> {
			if(ddlAuto.equals("create") || ddlAuto.equals("create-drop")) {
				Estudiante estudiante1 = new Estudiante();
				Estudiante estudiante2 = new Estudiante();
				Estudiante estudiante3 = new Estudiante();
				Usuario funcionario = new Funcionario();

				//-----USUARIOS-----
				PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

				if (usuarioRepository.findByCi("61647956").isEmpty()) {
					Usuario user = new Administrador();
					user.setNombre("Pedro");
					user.setApellido("Perez");
					user.setEmail("PedroPeAdmin@mail.com");
					user.setPassword(passwordEncoder.encode("1234"));
					user.setCi("61647956");
					user.setTelefono("091367159");
					user.setDomicilio("Lindoro Forteza 5548");
					//user.setFechaNac(LocalDate.of(1995, 6, 15));
					user.setIsEnable(true);
					user.setAccountNonExpired(true);
					user.setAccountNonLocked(true);
					user.setCredentialsNonExpired(true);
					usuarioRepository.save(user);
				}

				if (usuarioRepository.findByCi("42569843").isEmpty()) {
					estudiante1.setNombre("Luis");
					estudiante1.setApellido("Diaz");
					estudiante1.setEmail("LuisDiaz@mail.com");
					estudiante1.setPassword(passwordEncoder.encode("1234"));
					estudiante1.setCi("42569843");
					estudiante1.setTelefono("093874635");
					estudiante1.setDomicilio("Elias Regules 7898");
					//estudiante1.setFechaNac(LocalDate.of(1998, 5, 25));
					estudiante1.setIsEnable(true);
					estudiante1.setAccountNonExpired(true);
					estudiante1.setAccountNonLocked(true);
					estudiante1.setCredentialsNonExpired(true);
					usuarioRepository.save(estudiante1);

					estudiante2.setNombre("Horacio");
					estudiante2.setApellido("Hernandez");
					estudiante2.setEmail("HoriHernandez@mail.com");
					estudiante2.setPassword(passwordEncoder.encode("1234"));
					estudiante2.setCi("61253594");
					estudiante2.setTelefono("096558132");
					estudiante2.setDomicilio("Pitagoras 1122");
					//estudiante2.setFechaNac(LocalDate.of(1997, 8, 12));
					estudiante2.setIsEnable(true);
					estudiante2.setAccountNonExpired(true);
					estudiante2.setAccountNonLocked(true);
					estudiante2.setCredentialsNonExpired(true);
					usuarioRepository.save(estudiante2);

					estudiante3.setNombre("Ana Maria");
					estudiante3.setApellido("Diaz");
					estudiante3.setEmail("AnaMariaDiaz@mail.com");
					estudiante3.setPassword(passwordEncoder.encode("1234"));
					estudiante3.setCi("36984582");
					estudiante3.setTelefono("095664889");
					estudiante3.setDomicilio("Cmno Carrasco 2551");
					//estudiante3.setFechaNac(LocalDate.of(1996, 10, 30));
					estudiante3.setIsEnable(true);
					estudiante3.setAccountNonExpired(true);
					estudiante3.setAccountNonLocked(true);
					estudiante3.setCredentialsNonExpired(true);
					usuarioRepository.save(estudiante3);
				}
				if (usuarioRepository.findByCi("42687516").isEmpty()) {
					funcionario.setNombre("Carla");
					funcionario.setApellido("Miranda");
					funcionario.setEmail("CarlaMiranda@mail.com");
					funcionario.setPassword(passwordEncoder.encode("1234"));
					funcionario.setCi("42687516");
					funcionario.setTelefono("097895632");
					funcionario.setDomicilio("Bv Artigas 3556");
					//funcionario.setFechaNac(LocalDate.of(1990, 3, 10));
					funcionario.setIsEnable(true);
					funcionario.setAccountNonExpired(true);
					funcionario.setAccountNonLocked(true);
					funcionario.setCredentialsNonExpired(true);
					usuarioRepository.save(funcionario);
				}
				if (usuarioRepository.findByCi("31247689").isEmpty()) {
					Usuario coordinador = new Coordinador();
					coordinador.setNombre("Juan Pablo");
					coordinador.setApellido("Salinas");
					coordinador.setEmail("JuanPSalinas@mail.com");
					coordinador.setPassword(passwordEncoder.encode("1234"));
					coordinador.setCi("31247689");
					coordinador.setTelefono("094658759");
					coordinador.setDomicilio("Av Italia 6554");
					//coordinador.setFechaNac(LocalDate.of(1985, 12, 20));
					coordinador.setIsEnable(true);
					coordinador.setAccountNonExpired(true);
					coordinador.setAccountNonLocked(true);
					coordinador.setCredentialsNonExpired(true);
					usuarioRepository.save(coordinador);
				}

				//-----CARRERAS-----
				// Comprueba si la carrera ya existe antes de intentar insertarla
				Carrera savedCarrera1 = new Carrera();
				if (!carreraRepository.existsByNombre("Tecnologo informatico")) {
					Carrera carrera1 = new Carrera();
					carrera1.setNombre("Tecnologo informatico");
					carrera1.setDescripcion("Carrera de tecnologo informatico donde se enseña a programar en java, c++, c# y python");
					carrera1.setExistePlanEstudio(true);
					savedCarrera1 = carreraRepository.save(carrera1);
				}

				Carrera savedCarrera2 = new Carrera();
				if (!carreraRepository.existsByNombre("Diseño UX/UI")) {
					Carrera carrera2 = new Carrera();
					carrera2.setNombre("Diseño UX/UI");
					carrera2.setDescripcion("Carrera de diseño UX/UI donde se enseña a diseñar interfaces de usuario y experiencia de usuario");
					carrera2.setExistePlanEstudio(false);
					savedCarrera2 = carreraRepository.save(carrera2);
				}

				Carrera carrera3 = new Carrera();
				carrera3.setNombre("Tecnologo Audiovisual");
				carrera3.setDescripcion("Carrera de tecnologo audiovisual donde se enseña a editar videos y sonido");
				carrera3.setExistePlanEstudio(true);
				Carrera savedCarrera3 = carreraRepository.save(carrera3);

				//-----ASIGNATURAS-----
				// SOLO VERIFICO SI EXISTE COE COMO PARA DARME CUENTA SI SE INSERTO LA DUMMY DATA PREVIAMENTE O NO (PARA SABER SI SE EJECUTO CON CREATE O UPDATE)
				if (!asignaturaRepository.existsByNombre("Comunicacion oral y Escrita")) {
					createAsignaturaInitData(asignaturaRepository, "Comunicacion oral y Escrita", "Asignatura de comunicacion oral y escrita", 4, 3, savedCarrera1);
					createAsignaturaInitData(asignaturaRepository, "Matematica discreta y logica 1", "Conjuntos y subconjuntos", 3, 2 , savedCarrera1);
					createAsignaturaInitData(asignaturaRepository, "Programacion avanzada", "OOP con java", 2, 1 ,savedCarrera1);

					createAsignaturaInitData(asignaturaRepository, "Diseño de Interfaz", "Asignatura de diseño de interfaz de usuario", 4, 0, savedCarrera2);
					createAsignaturaInitData(asignaturaRepository, "Experiencia de Usuario", "Asignatura de experiencia de usuario", 3,  0, savedCarrera2);
					createAsignaturaInitData(asignaturaRepository, "Prototipado", "Asignatura de prototipado de interfaces", 2,  0, savedCarrera2);

					createAsignaturaInitData(asignaturaRepository, "Grabacion con camara PRO", "Grabacion Full HD 4k", 2, 1, savedCarrera3);
					createAsignaturaInitData(asignaturaRepository, "Adobe PRO", "Edicion de videos", 8, 2 , savedCarrera3);
					createAsignaturaInitData(asignaturaRepository, "Photoshop", "Photoshop con adobe ilustrator", 1, 3 ,savedCarrera3);
				}

				// -----TRAMITES-----
				createTramiteInitData(tramiteRepository, estudiante1, savedCarrera3, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.PENDIENTE,null);
				createTramiteInitData(tramiteRepository, estudiante1, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.PENDIENTE,null);
				createTramiteInitData(tramiteRepository, estudiante2, savedCarrera3, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.PENDIENTE,null);
				createTramiteInitData(tramiteRepository, estudiante2, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.PENDIENTE,null);

				createTramiteInitData(tramiteRepository, estudiante3, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario);
				createTramiteInitData(tramiteRepository, estudiante3, savedCarrera3, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO,funcionario);
				createTramiteInitData(tramiteRepository, estudiante3, savedCarrera1, TipoTramite.SOLICITUD_DE_TITULO, EstadoTramite.PENDIENTE,null);
				createTramiteInitData(tramiteRepository, estudiante3, savedCarrera3, TipoTramite.SOLICITUD_DE_TITULO, EstadoTramite.PENDIENTE,null);

				// -----INSCRIPCIONCARRERA-----
				InscripcionCarrera inscripcionCarrera1 = new InscripcionCarrera();
				inscripcionCarrera1.setEstudiante(estudiante3);
				inscripcionCarrera1.setCarrera(savedCarrera1);
				inscripcionCarrera1.setEstado(EstadoInscripcionCarrera.CURSANDO);
				inscripcionCarrera1.setFechaInscripcion(LocalDate.now());
				inscripcionCarreraRepository.save(inscripcionCarrera1);

				InscripcionCarrera inscripcionCarrera2= new InscripcionCarrera();
				inscripcionCarrera2.setEstudiante(estudiante3);
				inscripcionCarrera2.setCarrera(savedCarrera3);
				inscripcionCarrera2.setEstado(EstadoInscripcionCarrera.COMPLETADA);
				inscripcionCarrera2.setFechaInscripcion(LocalDate.now());
				inscripcionCarreraRepository.save(inscripcionCarrera2);

				// -----INSCRIPCIONCARRERA-----
				InscripcionCarrera inscripcionCarrera3 = new InscripcionCarrera();
				inscripcionCarrera3.setEstudiante(estudiante2);
				inscripcionCarrera3.setCarrera(savedCarrera3);
				inscripcionCarrera3.setEstado(EstadoInscripcionCarrera.CURSANDO);
				inscripcionCarrera3.setFechaInscripcion(LocalDate.now());
				inscripcionCarreraRepository.save(inscripcionCarrera3);

				//---- DOCENTES ----
				Docente docente1 = new Docente();
				docente1.setNombre("Fernando");
				docente1.setApellido("Garcia");
				docente1.setDocumento("42569897");
				docenteRepository.save(docente1);

				Docente docente2 = new Docente();
				docente2.setNombre("Manuel");
				docente2.setApellido("Gayoso");
				docente2.setDocumento("42585843");
				docenteRepository.save(docente2);

				Docente docente3 = new Docente();
				docente3.setNombre("Veronica");
				docente3.setApellido("Gonzalez");
				docente3.setDocumento("42569734");
				docenteRepository.save(docente3);

				//----- CURSOS -----
				Curso curso1 = new Curso();
				curso1.setEstado(Estado.ACTIVO);
				curso1.setAsignatura(asignaturaRepository.findById(1L).get());
				LocalDate fechaInicio = LocalDate.of(2025, 3, 15); // Año, Mes (1-12), Día
				curso1.setFechaInicio(fechaInicio);
				LocalDate fechaFin = LocalDate.of(2025, 07, 15); // Año, Mes (1-12), Día
				curso1.setFechaFin(fechaFin);
				curso1.setDocente(docente1);
				curso1.setDiasPrevInsc(3000);
				cursoRepository.save(curso1);

				Curso curso2 = new Curso();
				curso2.setEstado(Estado.FINALIZADO);
				curso2.setAsignatura(asignaturaRepository.findById(1L).get());
				fechaInicio = LocalDate.of(2023, 3, 15); // Año, Mes (1-12), Día
				curso2.setFechaInicio(fechaInicio);
				fechaFin = LocalDate.of(2023, 07, 15); // Año, Mes (1-12), Día
				curso2.setFechaFin(fechaFin);
				curso2.setDocente(docente1);
				curso2.setDiasPrevInsc(30);
				cursoRepository.save(curso2);

				//----- INSCRIPCIONCURSO -----
				InscripcionCurso inscripcionCurso1 = new InscripcionCurso();
				inscripcionCurso1.setFechaInscripcion(LocalDateTime.now());
				inscripcionCurso1.setEstudiante(estudiante2);
				inscripcionCurso1.setCurso(curso1);
				inscripcionCurso1.setCalificacion(CalificacionCurso.AEXAMEN);
				inscripcionCursoRepository.save(inscripcionCurso1);

				InscripcionCurso inscripcionCurso2 = new InscripcionCurso();
				inscripcionCurso2.setFechaInscripcion(LocalDateTime.now());
				inscripcionCurso2.setEstudiante(estudiante3);
				inscripcionCurso2.setCurso(curso1);
				inscripcionCurso2.setCalificacion(CalificacionCurso.AEXAMEN);
				inscripcionCursoRepository.save(inscripcionCurso2);

				//----- PERIODO EXAMEN -----
				PeriodoExamen periodoExamen1 = new PeriodoExamen();
				periodoExamen1.setFechaInicio(LocalDateTime.now().plusDays(5));
				periodoExamen1.setFechaFin(LocalDateTime.now().plusDays(10));
				periodoExamen1.setCarrera(carreraRepository.findById(1L).get());
				periodoExamenRepository.save(periodoExamen1);

				//----- EXAMEN -----
				Examen examen1 = new Examen();
				examen1.setFecha(LocalDateTime.now().plusDays(7));
				examen1.setDiasPrevInsc(20);
				examen1.setEstado(Estado.ACTIVO);
				examen1.setAsignatura(asignaturaRepository.findById(1L).get());
				examen1.setDocentes(new ArrayList<>());
				examen1.getDocentes().add(docenteRepository.findById(1L).get());
				examenRepository.save(examen1);

				Examen examen2 = new Examen();
				examen2.setFecha(LocalDateTime.now().minusDays(30));
				examen2.setDiasPrevInsc(20);
				examen2.setEstado(Estado.FINALIZADO);
				examen2.setAsignatura(asignaturaRepository.findById(1L).get());
				examen2.setDocentes(new ArrayList<>());
				examen2.getDocentes().add(docenteRepository.findById(1L).get());
				examenRepository.save(examen2);

				//----- INSCRIPCIONEXAMEN -----
				InscripcionExamen inscripcionExamen1 = new InscripcionExamen();
				inscripcionExamen1.setEstudiante(estudiante1);
				inscripcionExamen1.setExamen(examen1);
				inscripcionExamen1.setCalificacion(CalificacionExamen.PENDIENTE);
				inscripcionExamenRepository.save(inscripcionExamen1);

				InscripcionExamen inscripcionExamen2 = new InscripcionExamen();
				inscripcionExamen2.setEstudiante(estudiante2);
				inscripcionExamen2.setExamen(examen2);
				inscripcionExamen2.setCalificacion(CalificacionExamen.PENDIENTE);
				inscripcionExamenRepository.save(inscripcionExamen2);

			}
		};
	}

	private void createTramiteInitData(TramiteRepository tramiteRepository, Estudiante estudiante, Carrera savedCarrera, TipoTramite tipoTramite, EstadoTramite estadoTramite, Usuario usuarioResponsable) {
		Tramite tramite = new Tramite();
		tramite.setUsuarioSolicitante(estudiante);
		tramite.setTipo(tipoTramite);
		tramite.setEstado(estadoTramite);
		tramite.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
		tramite.setCarrera(savedCarrera);
		if(estadoTramite.equals(EstadoTramite.ACEPTADO) || estadoTramite.equals(EstadoTramite.RECHAZADO)){
			tramite.setUsuarioResponsable(usuarioResponsable);
		}
		tramiteRepository.save(tramite);
	}

	private void createAsignaturaInitData(AsignaturaRepository asignaturaRepository, String nombre, String descripcion, Integer creditos, Integer semestrePlanEstudio ,Carrera carrera) {
		if(!asignaturaRepository.existsByNombreAndCarreraId(nombre, carrera.getId())){

			Asignatura asignatura = new Asignatura(null, nombre, descripcion, creditos, semestrePlanEstudio, carrera, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
			asignaturaRepository.save(asignatura);
		}
	}
}
