package com.tecnoinf.gestedu;

import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GestionEducativaOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionEducativaOnlineApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UsuarioRepository usuarioRepository) {
		return args -> {

			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

			Usuario user = new Administrador();
			user.setNombre("admin");
			user.setApellido("admin");
			user.setEmail("silviajanet@gmail.com");
			user.setPassword(passwordEncoder.encode("1234"));
			user.setCi("123");
			user.setTelefono("1234567");
			user.setDomicilio("calle 123");
			user.setIsEnable(true);
			user.setAccountNonExpired(true);
			user.setAccountNonLocked(true);
			user.setCredentialsNonExpired(true);
			usuarioRepository.save(user);

			Usuario estudiante = new Estudiante();
			estudiante.setNombre("estudiante");
			estudiante.setApellido("estudiante");
			estudiante.setEmail("hola@hola1.com");
			estudiante.setPassword(passwordEncoder.encode("1234"));
			estudiante.setCi("1234");
			estudiante.setTelefono("1234567");
			estudiante.setDomicilio("calle 123");
			estudiante.setIsEnable(true);
			estudiante.setAccountNonExpired(true);
			estudiante.setAccountNonLocked(true);
			estudiante.setCredentialsNonExpired(true);
			usuarioRepository.save(estudiante);

			Usuario funcionario = new Funcionario();
			funcionario.setNombre("funcionario");
			funcionario.setApellido("funcionario");
			funcionario.setEmail("hola@hola2.com");
			funcionario.setPassword(passwordEncoder.encode("1234"));
			funcionario.setCi("12345");
			funcionario.setTelefono("1234567");
			funcionario.setDomicilio("calle 123");
			funcionario.setIsEnable(true);
			funcionario.setAccountNonExpired(true);
			funcionario.setAccountNonLocked(true);
			funcionario.setCredentialsNonExpired(true);
			usuarioRepository.save(funcionario);

			Usuario coordinador = new Coordinador();
			coordinador.setNombre("coordinador");
			coordinador.setApellido("coordinador");
			coordinador.setEmail("hola@hola3");
			coordinador.setPassword(passwordEncoder.encode("1234"));
			coordinador.setCi("123456");
			coordinador.setTelefono("1234567");
			coordinador.setDomicilio("calle 123");
			coordinador.setIsEnable(true);
			coordinador.setAccountNonExpired(true);
			coordinador.setAccountNonLocked(true);
			coordinador.setCredentialsNonExpired(true);
			usuarioRepository.save(coordinador);
		};
	}
}
