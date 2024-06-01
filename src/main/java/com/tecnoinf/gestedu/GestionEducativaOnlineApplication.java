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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@SpringBootApplication
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

				//-----USUARIOS-----
				PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

				if (usuarioRepository.findByCi("67141245").isEmpty()) {
					Usuario user = new Administrador();
					user.setNombre("NombreAdminInitData");
					user.setApellido("admin");
					user.setEmail("adminInitData@yahoo.com");
					user.setPassword(passwordEncoder.encode("1234"));
					user.setCi("67141245");
					user.setTelefono("1234567");
					user.setDomicilio("calle 123");
					user.setIsEnable(true);
					user.setAccountNonExpired(true);
					user.setAccountNonLocked(true);
					user.setCredentialsNonExpired(true);
					usuarioRepository.save(user);
				}

				if (usuarioRepository.findByCi("77141245").isEmpty()) {
					estudiante1.setNombre("Nombreestudiante1InitData");
					estudiante1.setApellido("estudiante1");
					estudiante1.setEmail("estudiante1InitData@yahoo.com");
					estudiante1.setPassword(passwordEncoder.encode("1234"));
					estudiante1.setCi("77141245");
					estudiante1.setTelefono("1234567");
					estudiante1.setDomicilio("calle 123");
					estudiante1.setIsEnable(true);
					estudiante1.setAccountNonExpired(true);
					estudiante1.setAccountNonLocked(true);
					estudiante1.setCredentialsNonExpired(true);
					usuarioRepository.save(estudiante1);

					estudiante2.setNombre("Nombreestudiante2InitData");
					estudiante2.setApellido("estudiante2");
					estudiante2.setEmail("estudiante2InitData@yahoo.com");
					estudiante2.setPassword(passwordEncoder.encode("1234"));
					estudiante2.setCi("77142245");
					estudiante2.setTelefono("1234567");
					estudiante2.setDomicilio("calle 123");
					estudiante2.setIsEnable(true);
					estudiante2.setAccountNonExpired(true);
					estudiante2.setAccountNonLocked(true);
					estudiante2.setCredentialsNonExpired(true);
					usuarioRepository.save(estudiante2);

					estudiante3.setNombre("Nombreestudiante3InitData");
					estudiante3.setApellido("estudiante3");
					estudiante3.setEmail("estudiante3InitData@yahoo.com");
					estudiante3.setPassword(passwordEncoder.encode("1234"));
					estudiante3.setCi("77143245");
					estudiante3.setTelefono("1234567");
					estudiante3.setDomicilio("calle 123");
					estudiante3.setIsEnable(true);
					estudiante3.setAccountNonExpired(true);
					estudiante3.setAccountNonLocked(true);
					estudiante3.setCredentialsNonExpired(true);
					usuarioRepository.save(estudiante3);
				}
				if (usuarioRepository.findByCi("87141245").isEmpty()) {
					Usuario funcionario = new Funcionario();
					funcionario.setNombre("NombreFuncionarioInitData");
					funcionario.setApellido("funcionario");
					funcionario.setEmail("funcionarioInitData@yahoo.com");
					funcionario.setPassword(passwordEncoder.encode("1234"));
					funcionario.setCi("87147245");
					funcionario.setTelefono("1234567");
					funcionario.setDomicilio("calle 123");
					funcionario.setIsEnable(true);
					funcionario.setAccountNonExpired(true);
					funcionario.setAccountNonLocked(true);
					funcionario.setCredentialsNonExpired(true);
					usuarioRepository.save(funcionario);
				}
				if (usuarioRepository.findByCi("97141245").isEmpty()) {
					Usuario coordinador = new Coordinador();
					coordinador.setNombre("NombreCoordinadorInitData");
					coordinador.setApellido("coordinador");
					coordinador.setEmail("coordinadorInitData@yahoo.com");
					coordinador.setPassword(passwordEncoder.encode("1234"));
					coordinador.setCi("97141245");
					coordinador.setTelefono("1234567");
					coordinador.setDomicilio("calle 123");
					coordinador.setIsEnable(true);
					coordinador.setAccountNonExpired(true);
					coordinador.setAccountNonLocked(true);
					coordinador.setCredentialsNonExpired(true);
					usuarioRepository.save(coordinador);
				}

				//-----CARRERAS-----
				// Comprueba si la carrera ya existe antes de intentar insertarla
				Carrera savedCarrera1 = new Carrera();
				if (!carreraRepository.existsByNombre("Tecnologo informatico InitData")) {
					Carrera carrera1 = new Carrera();
					carrera1.setNombre("Tecnologo informatico InitData");
					carrera1.setDescripcion("Carrera de tecnologo informatico donde se enseña a programar en java, c++, c# y python");
					carrera1.setExistePlanEstudio(true);
					savedCarrera1 = carreraRepository.save(carrera1);
				}

				Carrera savedCarrera2 = new Carrera();
				if (!carreraRepository.existsByNombre("Diseño UX/UI InitData")) {
					Carrera carrera2 = new Carrera();
					carrera2.setNombre("Diseño UX/UI InitData");
					carrera2.setDescripcion("Carrera de diseño UX/UI donde se enseña a diseñar interfaces de usuario y experiencia de usuario");
					carrera2.setExistePlanEstudio(false);
					savedCarrera2 = carreraRepository.save(carrera2);
				}

				Carrera carrera3 = new Carrera();
				carrera3.setNombre("Tecnologo Audiovisual InitData");
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

				// -----INSCRIPCIONCARRERA-----
				InscripcionCarrera inscripcionCarrera1 = new InscripcionCarrera();
				inscripcionCarrera1.setEstudiante(estudiante1);
				inscripcionCarrera1.setCarrera(savedCarrera1);
				inscripcionCarrera1.setEstado(EstadoInscripcionCarrera.CURSANDO);
				inscripcionCarrera1.setFechaInscripcion(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
				inscripcionCarreraRepository.save(inscripcionCarrera1);

				InscripcionCarrera inscripcionCarrera2= new InscripcionCarrera();
				inscripcionCarrera2.setEstudiante(estudiante2);
				inscripcionCarrera2.setCarrera(savedCarrera1);
				inscripcionCarrera2.setEstado(EstadoInscripcionCarrera.COMPLETADA);
				inscripcionCarrera2.setFechaInscripcion(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
				inscripcionCarreraRepository.save(inscripcionCarrera2);

				InscripcionCarrera inscripcionCarrera3 = new InscripcionCarrera();
				inscripcionCarrera3.setEstudiante(estudiante3);
				inscripcionCarrera3.setCarrera(savedCarrera1);
				inscripcionCarrera3.setEstado(EstadoInscripcionCarrera.CURSANDO);
				inscripcionCarrera3.setFechaInscripcion(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
				inscripcionCarreraRepository.save(inscripcionCarrera3);

				// -----TRAMITES-----
				Tramite tramite1 = new Tramite();
				tramite1.setUsuarioSolicitante(estudiante1);
				tramite1.setTipo(TipoTramite.INSCRIPCION_A_CARRERA);
				tramite1.setEstado(EstadoTramite.PENDIENTE);
				tramite1.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
				tramite1.setCarrera(savedCarrera3);
				tramiteRepository.save(tramite1);

				Tramite tramite2 = new Tramite();
				tramite2.setUsuarioSolicitante(estudiante2);
				tramite2.setTipo(TipoTramite.INSCRIPCION_A_CARRERA);
				tramite2.setEstado(EstadoTramite.PENDIENTE);
				tramite2.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
				tramite2.setCarrera(savedCarrera3);
				tramiteRepository.save(tramite2);

                //---- DOCENTES ----
				Docente docente1 = new Docente();
				docente1.setNombre("Docente1");
				docente1.setApellido("ApellidoDocente1");
				docente1.setDocumento("1234567");
				docenteRepository.save(docente1);

				Docente docente2 = new Docente();
				docente2.setNombre("Docente2");
				docente2.setApellido("ApellidoDocente2");
				docente2.setDocumento("1234568");
				docenteRepository.save(docente2);

				Docente docente3 = new Docente();
				docente3.setNombre("Docente3");
				docente3.setApellido("ApellidoDocente3");
				docente3.setDocumento("1234569");
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
				curso1.setDiasPrevInsc(30);
				cursoRepository.save(curso1);

				Curso curso2 = new Curso();
				curso2.setEstado(Estado.FINALIZADO);
				curso2.setAsignatura(asignaturaRepository.findById(1L).get());
				fechaInicio = LocalDate.of(2023, 3, 15); // Año, Mes (1-12), Día
				curso1.setFechaInicio(fechaInicio);
				fechaFin = LocalDate.of(2023, 07, 15); // Año, Mes (1-12), Día
				curso2.setFechaFin(fechaFin);
				curso2.setDocente(docente1);
				curso2.setDiasPrevInsc(30);
				cursoRepository.save(curso2);

				//----- INSCRIPCIONCURSO -----
				InscripcionCurso inscripcionCurso1 = new InscripcionCurso();
				inscripcionCurso1.setEstudiante(estudiante2);
				inscripcionCurso1.setCurso(curso1);
				inscripcionCurso1.setCalificacion(CalificacionCurso.AEXAMEN);
				inscripcionCursoRepository.save(inscripcionCurso1);

				InscripcionCurso inscripcionCurso2 = new InscripcionCurso();
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

	private void createAsignaturaInitData(AsignaturaRepository asignaturaRepository, String nombre, String descripcion, Integer creditos, Integer semestrePlanEstudio ,Carrera carrera) {
		if(!asignaturaRepository.existsByNombreAndCarreraId(nombre, carrera.getId())){

			Asignatura asignatura = new Asignatura(null, nombre, descripcion, creditos, semestrePlanEstudio, carrera, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
			asignaturaRepository.save(asignatura);
		}
	}
}
