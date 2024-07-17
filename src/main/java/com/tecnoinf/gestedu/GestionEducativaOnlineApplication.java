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
import java.time.ZoneId;
import java.util.TimeZone;
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

	@Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

	public static void main(String[] args) {
        SpringApplication.run(GestionEducativaOnlineApplication.class, args);
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("America/Montevideo")));
    }

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, CarreraRepository carreraRepository, AsignaturaRepository asignaturaRepository,
                                      InscripcionCarreraRepository inscripcionCarreraRepository, TramiteRepository tramiteRepository,
                                      DocenteRepository docenteRepository, CursoRepository cursoRepository, PeriodoExamenRepository periodoExamenRepository,
                                      ExamenRepository examenRepository, InscripcionCursoRepository inscripcionCursoRepository,
                                      InscripcionExamenRepository inscripcionExamenRepository, HorarioRepository horarioRepository, ActividadRepository actividadRepository) {

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
                createAndSaveUser(usuarioRepository, admin, "Pedro", "Perez", "PedroPeAdmin@mail.com", "1234", "61647956", "091367159", "Lindoro Forteza 5548", passwordEncoder, LocalDate.of(1990, 10, 10));
                createAndSaveUser(usuarioRepository, estudiante1, "Luis", "Diaz", "LuisDiaz@mail.com", "1234", "42569843", "093874635", "Elias Regules 7898", passwordEncoder, LocalDate.of(1995, 5, 15));
				createAndSaveUser(usuarioRepository, estudiante2, "Horacio", "Hernandez", "HoriHernandez@mail.com", "1234", "61253594", "096558132", "Pitagoras 1122", passwordEncoder, LocalDate.of(1996, 6, 20));
				createAndSaveUser(usuarioRepository, estudiante3, "Ana Maria", "Diaz", "AnaMariaDiaz@mail.com", "1234", "36984582", "095664889", "Cmno Carrasco 2551", passwordEncoder, LocalDate.of(1997, 7, 25));
				createAndSaveUser(usuarioRepository, estudiante4, "Lucas", "Hernandez", "LucasHernandez@mail.com", "1234", "36714582", "095554889", "Missipi 2551", passwordEncoder, LocalDate.of(1998, 8, 30));
				createAndSaveUser(usuarioRepository, estudiante5, "Serena", "Williams","SerenaWi@mail.com", "1234", "84714582", "095554871", "Gonzalo Ramirez 4795", passwordEncoder, LocalDate.of(1999, 9, 5));
				createAndSaveUser(usuarioRepository, estudiante6, "Agustin", "Cannobio","AgusCanobbio@mail.com", "1234", "15714582", "095554571", "Zum felde 1475", passwordEncoder, LocalDate.of(2000, 10, 10));
				createAndSaveUser(usuarioRepository, estudiante7, "Diego", "Forlan","DiegoForlan@mail.com", "1234", "84824582", "095144871", "Chacarita 1457", passwordEncoder, LocalDate.of(2001, 11, 15));
				createAndSaveUser(usuarioRepository, estudiante8, "Nicolas", "Badaracco","NicoBada@mail.com", "1234", "19514582", "096374871", "Porongos 4795", passwordEncoder, LocalDate.of(2002, 12, 20));
				createAndSaveUser(usuarioRepository, estudiante9, "Mauricio", "Mujica","MauriMu@mail.com", "1234", "48314582", "091654871", "Jose Cabrera 3335", passwordEncoder, LocalDate.of(2003, 1, 25));
				createAndSaveUser(usuarioRepository, funcionario1, "Carla", "Miranda", "CarlaMiranda@mail.com", "1234", "42687516", "097895632", "Bv Artigas 3556", passwordEncoder, LocalDate.of(1985, 2, 10));
				createAndSaveUser(usuarioRepository, funcionario2, "Andrew", "Garfield", "AndrewGarfield@mail.com", "1234", "42917516", "097145632", "Bv España 3556", passwordEncoder, LocalDate.of(1986, 3, 15));
				createAndSaveUser(usuarioRepository, coordinador1, "Juan Pablo", "Salinas", "JuanPSalinas@mail.com", "1234", "31247689", "094658759", "Av Italia 6554", passwordEncoder, LocalDate.of(1987, 4, 20));
				createAndSaveUser(usuarioRepository, coordinador2, "Fernando", "Dearrascaeta", "FerDearrasca@mail.com", "1234", "31177689", "094798759", "Ruta IB 6554", passwordEncoder, LocalDate.of(1988, 5, 25));

                //-----------------------------------------------------------------------------------CARRERAS--------------------------------------------------------------------------------------------------------------------
				Carrera savedCarrera1 = createAndSaveCarrera(carreraRepository, "Tecnologo informatico", "Carrera de tecnologo informatico donde se enseña a programar en java, c++, c# y python", true);
				Carrera savedCarrera2 = createAndSaveCarrera(carreraRepository, "Diseño UX/UI", "Carrera de diseño UX/UI donde se enseña a diseñar interfaces de usuario y experiencia de usuario", false);
				Carrera savedCarrera3 = createAndSaveCarrera(carreraRepository, "Tecnologo Audiovisual", "Carrera de tecnologo audiovisual donde se enseña a editar videos y sonido", true);

				createAndSaveActividad(actividadRepository, TipoActividad.ALTA_CARRERA, "Se ha creado la carrera " + savedCarrera1.getNombre(), coordinador1, LocalDateTime.of(2021, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository, TipoActividad.ALTA_CARRERA, "Se ha creado la carrera " + savedCarrera2.getNombre(), coordinador1, LocalDateTime.of(2021, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository, TipoActividad.ALTA_CARRERA, "Se ha creado la carrera " + savedCarrera3.getNombre(), coordinador1, LocalDateTime.of(2021, 5, 10, 10, 0));

                //----------------------------------------------------------------------------------ASIGNATURAS-------------------------------------------------------------------------------------------------------------
                Asignatura COE = createAsignaturaInitData(asignaturaRepository, "Comunicacion oral y Escrita", "Asignatura de comunicacion oral y escrita", 4, 3, savedCarrera1);
				Asignatura MDL1 = createAsignaturaInitData(asignaturaRepository, "Matematica discreta y logica 1", "Conjuntos y subconjuntos", 3, 2, savedCarrera1);
				Asignatura PAV = createAsignaturaInitData(asignaturaRepository, "Programacion avanzada", "OOP con java", 2, 1, savedCarrera1);

				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_ASIGNATURA, "Se ha creado la asignatura " + COE.getNombre() + " en la carrera " + savedCarrera1.getNombre() + " (id Carrera: " + savedCarrera1.getId() + ")", coordinador1, LocalDateTime.of(2021, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_ASIGNATURA, "Se ha creado la asignatura " + MDL1.getNombre() + " en la carrera " + savedCarrera1.getNombre() + " (id Carrera: " + savedCarrera1.getId() + ")", coordinador1, LocalDateTime.of(2021, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_ASIGNATURA, "Se ha creado la asignatura " + PAV.getNombre() + " en la carrera " + savedCarrera1.getNombre() + " (id Carrera: " + savedCarrera1.getId() + ")", coordinador1, LocalDateTime.of(2021, 5, 10, 10, 0));

                createAsignaturaInitData(asignaturaRepository, "Diseño de Interfaz", "Asignatura de diseño de interfaz de usuario", 4, 0, savedCarrera2);
                createAsignaturaInitData(asignaturaRepository, "Experiencia de Usuario", "Asignatura de experiencia de usuario", 3, 0, savedCarrera2);
                createAsignaturaInitData(asignaturaRepository, "Prototipado", "Asignatura de prototipado de interfaces", 2, 0, savedCarrera2);

				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_ASIGNATURA, "Se ha creado la asignatura " + "Diseño de Interfaz" + " en la carrera " + savedCarrera2.getNombre() + " (id Carrera: " + savedCarrera2.getId() + ")", coordinador1, LocalDateTime.of(2021, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_ASIGNATURA, "Se ha creado la asignatura " + "Experiencia de Usuario" + " en la carrera " + savedCarrera2.getNombre() + " (id Carrera: " + savedCarrera2.getId() + ")", coordinador1, LocalDateTime.of(2021, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_ASIGNATURA, "Se ha creado la asignatura " + "Prototipado" + " en la carrera " + savedCarrera2.getNombre() + " (id Carrera: " + savedCarrera2.getId() + ")", coordinador1, LocalDateTime.of(2021, 5, 10, 10, 0));

                createAsignaturaInitData(asignaturaRepository, "Grabacion con camara PRO", "Grabacion Full HD 4k", 2, 1, savedCarrera3);
                createAsignaturaInitData(asignaturaRepository, "Adobe PRO", "Edicion de videos", 8, 2, savedCarrera3);
                createAsignaturaInitData(asignaturaRepository, "Photoshop", "Photoshop con adobe ilustrator", 1, 3, savedCarrera3);

				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_ASIGNATURA, "Se ha creado la asignatura " + "Grabacion con camara PRO" + " en la carrera " + savedCarrera3.getNombre() + " (id Carrera: " + savedCarrera3.getId() + ")", coordinador1, LocalDateTime.of(2021, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_ASIGNATURA, "Se ha creado la asignatura " + "Adobe PRO" + " en la carrera " + savedCarrera3.getNombre() + " (id Carrera: " + savedCarrera3.getId() + ")", coordinador1, LocalDateTime.of(2021, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_ASIGNATURA, "Se ha creado la asignatura " + "Photoshop" + " en la carrera " + savedCarrera3.getNombre() + " (id Carrera: " + savedCarrera3.getId() + ")", coordinador1, LocalDateTime.of(2021, 5, 10, 10, 0));

                // -------------------------------------------------------------------------------------TRAMITES---------------------------------------------------------------------------------------------------
                //TRAMITES Y ACTIVIDADES DE INSCRIPCION A CARRERA DE LOS ESTUDIANTES 1 Y 2
				createTramiteInitData(tramiteRepository, estudiante1, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.PENDIENTE, null,"");
				createAndSaveActividad(actividadRepository,TipoActividad.SOLICITUD_INSCRIPCION_CARRERA, "Se solicito la inscripcion a la carrera " + savedCarrera1.getNombre(), estudiante1, LocalDateTime.of(2024, 5, 10, 10, 0));

				createTramiteInitData(tramiteRepository, estudiante2, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.PENDIENTE, null,"");
				createAndSaveActividad(actividadRepository,TipoActividad.SOLICITUD_INSCRIPCION_CARRERA, "Se solicito la inscripcion a la carrera " + savedCarrera1.getNombre(), estudiante2, LocalDateTime.of(2024, 5, 10, 10, 0));

				//ACTIVIDAD SOLICITUD DE INSCRIPCION A CARRERA DE LOS ESTUDIANTES 3 AL 9
				createAndSaveActividad(actividadRepository,TipoActividad.SOLICITUD_INSCRIPCION_CARRERA, "Se solicito la inscripcion a la carrera " + savedCarrera1.getNombre(), estudiante3, LocalDateTime.of(2024, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.SOLICITUD_INSCRIPCION_CARRERA, "Se solicito la inscripcion a la carrera " + savedCarrera1.getNombre(), estudiante4, LocalDateTime.of(2024, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.SOLICITUD_INSCRIPCION_CARRERA, "Se solicito la inscripcion a la carrera " + savedCarrera1.getNombre(), estudiante5, LocalDateTime.of(2024, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.SOLICITUD_INSCRIPCION_CARRERA, "Se solicito la inscripcion a la carrera " + savedCarrera1.getNombre(), estudiante7, LocalDateTime.of(2024, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.SOLICITUD_INSCRIPCION_CARRERA, "Se solicito la inscripcion a la carrera " + savedCarrera1.getNombre(), estudiante8, LocalDateTime.of(2024, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.SOLICITUD_INSCRIPCION_CARRERA, "Se solicito la inscripcion a la carrera " + savedCarrera1.getNombre(), estudiante9, LocalDateTime.of(2024, 5, 10, 10, 0));

				//ACTIVIDAD SE ACEPTA LA SOLICITUD DE INSCRIPCION A CARRERA DE LOS ESTUDIANTES 3 AL 9
				createAndSaveActividad(actividadRepository,TipoActividad.APROBACION_SOLICITUD_INSCRIPCION_CARRERA, "Se aprobo la solicitud de inscripcion del estudiante " + estudiante3.getNombre() + " " + estudiante3.getApellido() + " a la carrera " + savedCarrera1.getNombre(), funcionario1, LocalDateTime.of(2024, 5, 12, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.APROBACION_SOLICITUD_INSCRIPCION_CARRERA, "Se aprobo la solicitud de inscripcion del estudiante " + estudiante4.getNombre() + " " + estudiante4.getApellido() + " a la carrera " + savedCarrera1.getNombre(), funcionario1, LocalDateTime.of(2024, 5, 12, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.APROBACION_SOLICITUD_INSCRIPCION_CARRERA, "Se aprobo la solicitud de inscripcion del estudiante " + estudiante5.getNombre() + " " + estudiante5.getApellido() + " a la carrera " + savedCarrera1.getNombre(), funcionario1, LocalDateTime.of(2024, 5, 12, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.APROBACION_SOLICITUD_INSCRIPCION_CARRERA, "Se aprobo la solicitud de inscripcion del estudiante " + estudiante7.getNombre() + " " + estudiante7.getApellido() + " a la carrera " + savedCarrera1.getNombre(), funcionario1, LocalDateTime.of(2024, 5, 12, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.APROBACION_SOLICITUD_INSCRIPCION_CARRERA, "Se aprobo la solicitud de inscripcion del estudiante " + estudiante8.getNombre() + " " + estudiante8.getApellido() + " a la carrera " + savedCarrera1.getNombre(), funcionario1, LocalDateTime.of(2024, 5, 12, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.APROBACION_SOLICITUD_INSCRIPCION_CARRERA, "Se aprobo la solicitud de inscripcion del estudiante " + estudiante9.getNombre() + " " + estudiante9.getApellido() + " a la carrera " + savedCarrera1.getNombre(), funcionario1, LocalDateTime.of(2024, 5, 12, 10, 0));

				//TRAMITES RESULTANTES DE LA SOLICITUD DE INSCRIPCION A CARRERA DE LOS ESTUDIANTES 3 AL 9
				createTramiteInitData(tramiteRepository, estudiante3, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario1,"");
				createTramiteInitData(tramiteRepository, estudiante4, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario1,"");
				createTramiteInitData(tramiteRepository, estudiante5, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario1,"");
				createTramiteInitData(tramiteRepository, estudiante7, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario1,"");
				createTramiteInitData(tramiteRepository, estudiante8, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario1,"");
				createTramiteInitData(tramiteRepository, estudiante9, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.ACEPTADO, funcionario1,"");

				//TRAMITE Y ACTIVIDAD DE LA SOLICITUD DE INSCRIPCION A CARRERA DEL ESTUDIANTE 6 (SE RECHAZA)
				createTramiteInitData(tramiteRepository, estudiante6, savedCarrera1, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.RECHAZADO, funcionario1,"Faltante de documentacion");
				createAndSaveActividad(actividadRepository,TipoActividad.SOLICITUD_INSCRIPCION_CARRERA, "Se solicito la inscripcion a la carrera " + savedCarrera1.getNombre(), estudiante6, LocalDateTime.of(2024, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.RECHAZO_SOLICITUD_INSCRIPCION_CARRERA, "Se rechazo la solicitud de inscripcion del estudiante " + estudiante6.getNombre() + " " + estudiante6.getApellido() + " a la carrera " + savedCarrera1.getNombre(), funcionario1, LocalDateTime.of(2024, 5, 12, 10, 0));

				//TRAMITE Y ACTIVIDAD DE LA SOLICITUD DE TITULO DE LOS ESTUDIANTES 4, 5 y 9 (el 9 pide el titulo pero le faltan creditos)
				createTramiteInitData(tramiteRepository, estudiante4, savedCarrera1, TipoTramite.SOLICITUD_DE_TITULO, EstadoTramite.ACEPTADO, coordinador1,"");
				createAndSaveActividad(actividadRepository,TipoActividad.SOLICITUD_TITULO, "Se solicito el titulo de la carrera " + savedCarrera1.getNombre(), estudiante4, LocalDateTime.of(2024, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.APROBACION_SOLICITUD_TITULO, "Se aprobo la solicitud de titulo del estudiante " + estudiante4.getNombre() + " " + estudiante4.getApellido() + " de la carrera " + savedCarrera1.getNombre(), coordinador1, LocalDateTime.of(2024, 5, 12, 10, 0));

				createTramiteInitData(tramiteRepository, estudiante5, savedCarrera1, TipoTramite.SOLICITUD_DE_TITULO, EstadoTramite.RECHAZADO, coordinador2,"No se tienen los creditos necesarios para solicitar el titulo");
				createAndSaveActividad(actividadRepository,TipoActividad.SOLICITUD_TITULO, "Se solicito el titulo de la carrera " + savedCarrera1.getNombre(), estudiante5, LocalDateTime.of(2024, 5, 10, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.RECHAZO_SOLICITUD_TITULO, "Se rechazo la solicitud de titulo del estudiante " + estudiante5.getNombre() + " " + estudiante5.getApellido() + " de la carrera " + savedCarrera1.getNombre(), coordinador2, LocalDateTime.of(2024, 5, 12, 10, 0));

				createTramiteInitData(tramiteRepository, estudiante9, savedCarrera1, TipoTramite.SOLICITUD_DE_TITULO, EstadoTramite.PENDIENTE, coordinador1,"");
				createAndSaveActividad(actividadRepository,TipoActividad.SOLICITUD_TITULO, "Se solicito el titulo de la carrera " + savedCarrera1.getNombre(), estudiante9, LocalDateTime.of(2024, 5, 10, 10, 0));

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

				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_DOCENTE, "Se ha creado el docente con nombre" + docente1.getNombre(), funcionario1, LocalDateTime.of(2022, 3, 20, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_DOCENTE, "Se ha creado el docente con nombre" + docente2.getNombre(), funcionario1, LocalDateTime.of(2022, 3, 20, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_DOCENTE, "Se ha creado el docente con nombre" + docente3.getNombre(), funcionario1, LocalDateTime.of(2022, 3, 20, 10, 0));

                //----------------------------------------------------------------------------------------- CURSOS -----------------------------------------------------------------------------------------
				//TECNOLOGO INFORMATICO

				//CURSOS FINALIZADOS (VIEJOS)
				Curso cursoCOEViejo = createAndSaveCurso(cursoRepository, Estado.FINALIZADO, COE, LocalDate.of(2023, 3, 15), LocalDate.of(2023, 6, 15), docente1, 30, false);
				Curso cursoMDL1Viejo = createAndSaveCurso(cursoRepository, Estado.FINALIZADO, MDL1, LocalDate.of(2023, 3, 15), LocalDate.of(2023, 6, 15), docente2, 30, false);
				Curso cursoPAVViejo = createAndSaveCurso(cursoRepository, Estado.FINALIZADO, PAV, LocalDate.of(2023, 7, 15), LocalDate.of(2023, 11, 15), docente3, 30, false);
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_CURSO, "Se ha creado un curso para la asignatura" + COE.getNombre() + " con fecha de inicio " + cursoCOEViejo.getFechaInicio() + " y fecha de fin " + cursoCOEViejo.getFechaFin(), funcionario1, LocalDateTime.of(2023, 3, 8, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_CURSO, "Se ha creado un curso para la asignatura" + MDL1.getNombre() + " con fecha de inicio " + cursoMDL1Viejo.getFechaInicio() + " y fecha de fin " + cursoMDL1Viejo.getFechaFin(), funcionario1, LocalDateTime.of(2023, 3, 8, 10, 0));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_CURSO, "Se ha creado un curso para la asignatura" + PAV.getNombre() + " con fecha de inicio " + cursoPAVViejo.getFechaInicio() + " y fecha de fin " + cursoPAVViejo.getFechaFin(), funcionario1, LocalDateTime.of(2023, 6, 8, 10, 0));

				//CURSOS ACTIVOS (VIEJOS)
				Curso cursoMDL2Viejo = createAndSaveCurso(cursoRepository, Estado.ACTIVO, MDL1, LocalDate.of(2023, 7, 15), LocalDate.of(2023, 11, 15), docente2, 30, false);
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_CURSO, "Se ha creado un curso para la asignatura" + MDL1.getNombre() + " con fecha de inicio " + cursoMDL2Viejo.getFechaInicio() + " y fecha de fin " + cursoMDL2Viejo.getFechaFin(), funcionario1, LocalDateTime.of(2023, 6, 8, 10, 0));

				//CURSOS ACTIVOS (FECHAS DINAMICAS)
				Curso cursoCOE = createAndSaveCurso(cursoRepository, Estado.ACTIVO, COE, LocalDate.now().plusDays(20), LocalDate.now().plusDays(20).plusMonths(4), docente1, 30, true);
				Curso cursoMDL1 = createAndSaveCurso(cursoRepository, Estado.ACTIVO, MDL1, LocalDate.now().plusDays(20), LocalDate.now().plusDays(20).plusMonths(4), docente2, 30, true);
				Curso cursoPAV = createAndSaveCurso(cursoRepository, Estado.ACTIVO, PAV, LocalDate.now().plusDays(20), LocalDate.now().plusDays(20).plusMonths(4), docente3, 30, true);
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_CURSO, "Se ha creado un curso para la asignatura" + COE.getNombre() + " con fecha de inicio " + cursoCOE.getFechaInicio() + " y fecha de fin " + cursoCOE.getFechaFin(), funcionario1, LocalDateTime.now().minusDays(20));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_CURSO, "Se ha creado un curso para la asignatura" + MDL1.getNombre() + " con fecha de inicio " + cursoMDL1.getFechaInicio() + " y fecha de fin " + cursoMDL1.getFechaFin(), funcionario1, LocalDateTime.now().minusDays(20));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_CURSO, "Se ha creado un curso para la asignatura" + PAV.getNombre() + " con fecha de inicio " + cursoPAV.getFechaInicio() + " y fecha de fin " + cursoPAV.getFechaFin(), funcionario1, LocalDateTime.now().minusDays(20));

				//HORARIOS
				createAndSaveHorario(horarioRepository,DiaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0), cursoCOE);
				createAndSaveHorario(horarioRepository,DiaSemana.MIERCOLES, LocalTime.of(10, 0), LocalTime.of(12, 0), cursoCOE);
				createAndSaveHorario(horarioRepository,DiaSemana.MARTES, LocalTime.of(9, 0), LocalTime.of(11, 0), cursoMDL1);
				createAndSaveHorario(horarioRepository,DiaSemana.JUEVES, LocalTime.of(11, 0), LocalTime.of(13, 0), cursoMDL1);
				createAndSaveHorario(horarioRepository,DiaSemana.MIERCOLES, LocalTime.of(8, 0), LocalTime.of(10, 0), cursoPAV);
				createAndSaveHorario(horarioRepository,DiaSemana.VIERNES, LocalTime.of(10, 0), LocalTime.of(12, 0), cursoPAV);
				createAndSaveActividad(actividadRepository,TipoActividad.REGISTRO_HORARIOS_CURSO, "Se ha registrado un horario para el curso con id " + cursoCOE.getId(), funcionario1, LocalDateTime.now().minusDays(20));
				createAndSaveActividad(actividadRepository,TipoActividad.REGISTRO_HORARIOS_CURSO, "Se ha registrado un horario para el curso con id " + cursoMDL1.getId(), funcionario1, LocalDateTime.now().minusDays(20));
				createAndSaveActividad(actividadRepository,TipoActividad.REGISTRO_HORARIOS_CURSO, "Se ha registrado un horario para el curso con id " + cursoPAV.getId(), funcionario1, LocalDateTime.now().minusDays(20));
				createAndSaveActividad(actividadRepository,TipoActividad.REGISTRO_HORARIOS_CURSO, "Se ha registrado un horario para el curso con id " + cursoCOE.getId(), funcionario1, LocalDateTime.now().minusDays(20));
				createAndSaveActividad(actividadRepository,TipoActividad.REGISTRO_HORARIOS_CURSO, "Se ha registrado un horario para el curso con id " + cursoMDL1.getId(), funcionario1, LocalDateTime.now().minusDays(20));
				createAndSaveActividad(actividadRepository,TipoActividad.REGISTRO_HORARIOS_CURSO, "Se ha registrado un horario para el curso con id " + cursoPAV.getId(), funcionario1, LocalDateTime.now().minusDays(20));

				//CURSOS FUTUROS (NO SE PUEDE INSCRIBIR AUN)
				Curso cursoCOEFuturo = createAndSaveCurso(cursoRepository, Estado.ACTIVO, COE, LocalDate.now().plusDays(40), LocalDate.now().plusDays(40).plusMonths(4), docente1, 30, false);
				Curso cursoMDL1Futuro = createAndSaveCurso(cursoRepository, Estado.ACTIVO, MDL1, LocalDate.now().plusDays(40), LocalDate.now().plusDays(40).plusMonths(4), docente2, 30, false);
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_CURSO, "Se ha creado un curso para la asignatura" + COE.getNombre() + " con fecha de inicio " + cursoCOEFuturo.getFechaInicio() + " y fecha de fin " + cursoCOEFuturo.getFechaFin(), funcionario1, LocalDateTime.now().minusDays(20));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_CURSO, "Se ha creado un curso para la asignatura" + MDL1.getNombre() + " con fecha de inicio " + cursoMDL1Futuro.getFechaInicio() + " y fecha de fin " + cursoMDL1Futuro.getFechaFin(), funcionario1, LocalDateTime.now().minusDays(20));

				//------------------------------------------------------------------------------- INSCRIPCIONCURSO -------------------------------------------------------------------------------
				// INSCRIPCIONES CURSOS VIEJOS QUE EXONERO EL ESTUDIANTE 7 (TIENE TODOS LOS CREDITOS PARA SOLICITAR EL TITULO)
				InscripcionCurso inscripcionCurso1 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante7, cursoCOEViejo, EstadoInscripcionCurso.COMPLETADA, CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10) );
				InscripcionCurso inscripcionCurso2 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante7, cursoMDL1Viejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10));
				InscripcionCurso inscripcionCurso3 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante7, cursoPAVViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoCOEViejo.getId() , estudiante7, LocalDateTime.of(2023,7,10,0,0));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoMDL1Viejo.getId(), estudiante7, LocalDateTime.of(2023,7,10,0,0));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoPAVViejo.getId(), estudiante7, LocalDateTime.of(2023,7,10,0,0));

				// INSCRIPCIONES CURSOS VIEJOS QUE EXONERO EL ESTUDIANTE 4 (Ya tiene el titulo solicitado y aceptado)
				InscripcionCurso inscripcionCurso13 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante4, cursoCOEViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10));
				InscripcionCurso inscripcionCurso14 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante4, cursoMDL1Viejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10));
				InscripcionCurso inscripcionCurso15 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante4, cursoPAVViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoCOEViejo.getId(), estudiante4, LocalDateTime.of(2023,7,10,0,0));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoMDL1Viejo.getId(), estudiante4, LocalDateTime.of(2023,7,10,0,0));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoPAVViejo.getId(), estudiante4, LocalDateTime.of(2023,7,10,0,0));

				// INSCRIPCIONES A CURSO EN DISTINTOS ESTADOS
				InscripcionCurso inscripcionCurso4 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante5, cursoCOEViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.AEXAMEN,LocalDate.of(2023, 7, 10));
				//InscripcionCurso inscripcionCurso5 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante5, cursoMDL1, EstadoInscripcionCurso.CURSANDO,CalificacionCurso.PENDIENTE, LocalDate.now());
				InscripcionCurso inscripcionCurso6 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante5, cursoPAVViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.AEXAMEN, LocalDate.of(2023, 7, 10));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoCOEViejo.getId(), estudiante5, LocalDateTime.of(2023,7,10,0,0));
				//createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoMDL1.getId(), estudiante5, LocalDateTime.of(2023,7,10,0,0));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoPAVViejo.getId(), estudiante5, LocalDateTime.of(2023,7,10,0,0));

				InscripcionCurso inscripcionCurso7 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante8, cursoCOEViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10));
				InscripcionCurso inscripcionCurso8 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante8, cursoMDL1Viejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.AEXAMEN, LocalDate.of(2023, 7, 10));
				InscripcionCurso inscripcionCurso9 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante8, cursoPAVViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.AEXAMEN, LocalDate.of(2023, 7, 10));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoCOEViejo.getId(), estudiante8, LocalDateTime.of(2023,7,10,0,0));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoMDL1Viejo.getId(), estudiante8, LocalDateTime.of(2023,7,10,0,0));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoPAVViejo.getId(), estudiante8, LocalDateTime.of(2023,7,10,0,0));

				InscripcionCurso inscripcionCurso10 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante9, cursoCOEViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.RECURSA, LocalDate.of(2023, 7, 10));
				InscripcionCurso inscripcionCurso11 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante9, cursoMDL1, EstadoInscripcionCurso.CURSANDO,CalificacionCurso.PENDIENTE, LocalDate.now());
				InscripcionCurso inscripcionCurso12 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante9, cursoPAVViejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.EXONERADO, LocalDate.of(2023, 7, 10));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoCOEViejo.getId(), estudiante9, LocalDateTime.of(2023,7,10,0,0));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoMDL1.getId(), estudiante9, LocalDateTime.of(2023,7,10,0,0));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoPAVViejo.getId(), estudiante9, LocalDateTime.of(2023,7,10,0,0));

				//inscripcion a curso viejo pendiente de cailficar
				InscripcionCurso inscripcionCurso16 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante8, cursoMDL2Viejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.PENDIENTE, LocalDate.of(2023, 7, 13));
				InscripcionCurso inscripcionCurso17 = createAndSaveInscripcionCurso(inscripcionCursoRepository, estudiante9, cursoMDL2Viejo, EstadoInscripcionCurso.COMPLETADA,CalificacionCurso.PENDIENTE, LocalDate.of(2023, 7, 13));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoMDL2Viejo.getId(), estudiante8, LocalDateTime.of(2023,7,13,0,0));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " +cursoMDL2Viejo.getId(), estudiante9, LocalDateTime.of(2023,7,13,0,0));

				//ESTUDIANTE 3 NO ESTA INSCRIPTO A NINGUN CURSO

                //------------------------------------------------------------------------------- PERIODO EXAMEN ----------------------------------------------------------------------------------
				PeriodoExamen periodoExamen1 = createAndSavePeriodoExamen(periodoExamenRepository, savedCarrera1, 5, 10);
				createAndSaveActividad(actividadRepository,TipoActividad.REGISTRO_PERIODO_EXAMEN, "Se ha registrado un nuevo periodo de examen para la carrera con id " + savedCarrera1.getId() + " con fecha de inicio " + LocalDateTime.now().plusDays(5) + " y fecha de fin " + LocalDateTime.now().plusDays(10), funcionario1, LocalDateTime.now().minusDays(20));

                //------------------------------------------------------------------------------------- EXAMEN -------------------------------------------------------------------------------------
				// ACTIVOS
				Examen examenCOE = createAndSaveExamen(examenRepository, LocalDateTime.now().plusDays(7).truncatedTo(ChronoUnit.HOURS), 20, Estado.ACTIVO, COE, docente1);
				Examen examenPAV = createAndSaveExamen(examenRepository, LocalDateTime.now().plusDays(0).truncatedTo(ChronoUnit.HOURS), 20, Estado.ACTIVO, PAV, docente3);
				Examen examenMDL1 = createAndSaveExamen(examenRepository, LocalDateTime.now().minusDays(3).truncatedTo(ChronoUnit.HOURS), 20, Estado.ACTIVO, MDL1, docente2);

				// FINALIZADOS
				Examen examenMDL1Finalizado = createAndSaveExamen(examenRepository, LocalDateTime.now().minusMonths(5).truncatedTo(ChronoUnit.HOURS), 20, Estado.FINALIZADO, MDL1, docente2);
				Examen examenPAVFinalizado = createAndSaveExamen(examenRepository, LocalDateTime.now().minusMonths(5).truncatedTo(ChronoUnit.HOURS), 20, Estado.FINALIZADO, PAV, docente3);
				Examen examenCOEFinalizado = createAndSaveExamen(examenRepository, LocalDateTime.now().minusMonths(5).truncatedTo(ChronoUnit.HOURS), 20, Estado.FINALIZADO, COE, docente1);
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_EXAMEN, "Se ha creado un examen para la asignatura" + MDL1.getNombre() + " con fecha de inicio " + examenMDL1Finalizado.getFecha(), funcionario1, LocalDateTime.now().minusMonths(5).minusDays(20));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_EXAMEN, "Se ha creado un examen para la asignatura" + PAV.getNombre() + " con fecha de inicio " + examenPAVFinalizado.getFecha(), funcionario1, LocalDateTime.now().minusMonths(5).minusDays(20));
				createAndSaveActividad(actividadRepository,TipoActividad.ALTA_EXAMEN, "Se ha creado un examen para la asignatura" + COE.getNombre() + " con fecha de inicio " + examenCOEFinalizado.getFecha(), funcionario1, LocalDateTime.now().minusMonths(5).minusDays(20));

                //----- INSCRIPCIONEXAMEN -----
				InscripcionExamen inscripcionExamen1 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante5, examenPAVFinalizado, CalificacionExamen.REPROBADO, LocalDate.now().minusMonths(5).minusDays(2));
				InscripcionExamen inscripcionExamen2 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante5, examenPAV, CalificacionExamen.PENDIENTE,LocalDate.now());
				//InscripcionExamen inscripcionExamen7 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante5, examenCOEFinalizado, CalificacionExamen.APROBADO,LocalDate.now().minusMonths(5).minusDays(2));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_EXAMEN, "Inscripcion a examen con id " + examenPAVFinalizado.getId() + " exitosa.", estudiante5, LocalDateTime.now().minusMonths(5).minusDays(2));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_EXAMEN, "Inscripcion a examen con id " + examenPAV.getId() + " exitosa.", estudiante5, LocalDateTime.now());
				//createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_EXAMEN, "Inscripcion a examen con id " + examenCOEFinalizado.getId() + " exitosa.", estudiante5, LocalDateTime.now().minusMonths(5).minusDays(2));

				InscripcionExamen inscripcionExamen3 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante8, examenPAVFinalizado, CalificacionExamen.REPROBADO,LocalDate.now().minusMonths(5).minusDays(2));
				InscripcionExamen inscripcionExamen4 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante8, examenPAV, CalificacionExamen.PENDIENTE,LocalDate.now());
				InscripcionExamen inscripcionExamen5 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante8, examenMDL1Finalizado, CalificacionExamen.REPROBADO,LocalDate.now().minusMonths(5).minusDays(2));
				InscripcionExamen inscripcionExamen6 = createAndSaveInscripcionExamen(inscripcionExamenRepository, estudiante8, examenMDL1, CalificacionExamen.PENDIENTE,LocalDate.now());
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_EXAMEN, "Inscripcion a examen con id " + examenPAVFinalizado.getId() + " exitosa.", estudiante8, LocalDateTime.now().minusMonths(5).minusDays(2));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_EXAMEN, "Inscripcion a examen con id " + examenPAV.getId() + " exitosa.", estudiante8, LocalDateTime.now());
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_EXAMEN, "Inscripcion a examen con id " + examenMDL1Finalizado.getId() + " exitosa.", estudiante8, LocalDateTime.now().minusMonths(5).minusDays(2));
				createAndSaveActividad(actividadRepository,TipoActividad.INSCRIPCION_A_EXAMEN, "Inscripcion a examen con id " + examenMDL1.getId() + " exitosa.", estudiante8, LocalDateTime.now());
            }
        };
    }

	private void createAndSaveUser(UsuarioRepository usuarioRepository, Usuario user, String nombre, String apellido, String email, String password,
								   String ci, String telefono, String domicilio, PasswordEncoder passwordEncoder, LocalDate fechaNacimiento) {
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
		user.setFechaNac(fechaNacimiento);
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

	private Curso createAndSaveCurso(CursoRepository cursoRepository, Estado estado, Asignatura asignatura, LocalDate fechaInicio, LocalDate fechaFin, Docente docente, int diasPrevInsc, Boolean horario) {
		Curso curso = new Curso();
		curso.setEstado(estado);
		curso.setAsignatura(asignatura);
		curso.setFechaInicio(fechaInicio);
		curso.setFechaFin(fechaFin);
		curso.setDocente(docente);
		curso.setDiasPrevInsc(diasPrevInsc);
		curso.setHorario(horario);
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

	private PeriodoExamen createAndSavePeriodoExamen(PeriodoExamenRepository periodoExamenRepository, Carrera carrera, int startDays, int endDays) {
		PeriodoExamen periodoExamen = new PeriodoExamen();
		periodoExamen.setFechaInicio(LocalDate.now().plusDays(startDays));
		periodoExamen.setFechaFin(LocalDate.now().plusDays(endDays));
		periodoExamen.setCarrera(carrera);
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

	private InscripcionExamen createAndSaveInscripcionExamen(InscripcionExamenRepository inscripcionExamenRepository, Estudiante estudiante, Examen examen, CalificacionExamen calificacion, LocalDate fechaInscripcion) {
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

	private Actividad createAndSaveActividad(ActividadRepository actividadRepository, TipoActividad tipoActividad, String descripcion, Usuario usuario, LocalDateTime fechaCreacion) {
		Actividad actividad = new Actividad();
		actividad.setTipoActividad(tipoActividad);
		actividad.setDescripcion(descripcion);
		actividad.setUsuario(usuario);
		actividad.setFecha(fechaCreacion);
		return actividadRepository.save(actividad);
	}

}
