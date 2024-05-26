package com.tecnoinf.gestedu;

import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCarrera;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.InscripcionCarreraRepository;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
									  InscripcionCarreraRepository inscripcionCarreraRepository) {
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

				//-----ASIGNATURAS-----
				// SOLO VERIFICO SI EXISTE COE COMO PARA DARME CUENTA SI SE INSERTO LA DUMMY DATA PREVIAMENTE O NO (PARA SABER SI SE EJECUTO CON CREATE O UPDATE)
				if (!asignaturaRepository.existsByNombre("Comunicacion oral y Escrita")) {
					createAsignaturaInitData(asignaturaRepository, "Comunicacion oral y Escrita", "Asignatura de comunicacion oral y escrita", 4, 3, savedCarrera1);
					createAsignaturaInitData(asignaturaRepository, "Matematica discreta y logica 1", "Conjuntos y subconjuntos", 3, 2 , savedCarrera1);
					createAsignaturaInitData(asignaturaRepository, "Programacion avanzada", "OOP con java", 2, 1 ,savedCarrera1);

					createAsignaturaInitData(asignaturaRepository, "Diseño de Interfaz", "Asignatura de diseño de interfaz de usuario", 4, 0, savedCarrera2);
					createAsignaturaInitData(asignaturaRepository, "Experiencia de Usuario", "Asignatura de experiencia de usuario", 3,  0, savedCarrera2);
					createAsignaturaInitData(asignaturaRepository, "Prototipado", "Asignatura de prototipado de interfaces", 2,  0, savedCarrera2);
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

			}
		};
	}

	private void createAsignaturaInitData(AsignaturaRepository asignaturaRepository, String nombre, String descripcion, Integer creditos, Integer semestrePlanEstudio ,Carrera carrera) {
		if(!asignaturaRepository.existsByNombreAndCarreraId(nombre, carrera.getId())){
			Asignatura asignatura = new Asignatura(null, nombre, descripcion, creditos, semestrePlanEstudio, carrera, new ArrayList<>());
			asignaturaRepository.save(asignatura);
		}
	}
}
