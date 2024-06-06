package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.EmailValuesDTO;
import com.tecnoinf.gestedu.dtos.usuario.AuthLoginRequest;
import com.tecnoinf.gestedu.dtos.usuario.AuthResponse;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.services.interfaces.EmailService;
import com.tecnoinf.gestedu.services.interfaces.InvitadoService;
import com.tecnoinf.gestedu.util.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Service
public class InvitadoServiceImpl implements InvitadoService {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    EmailService emailService;

    @Value("${mail.urlFront}")
    private String urlFront;

    @Override
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        //generar token de acceso
        String email = authLoginRequest.email();
        String password = authLoginRequest.password();

        Authentication authentication = this.autenticar(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.crearToken(authentication);

        // Obtener las autoridades del usuario
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> authorityNames = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            authorityNames.add(authority.getAuthority());
        }
        System.out.println("Autoridades del usuario: " + authorityNames);

        AuthResponse authResponse = new AuthResponse(email, "Usuario logueado", token, true);
        return authResponse;
    }

    public Authentication autenticar(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if(userDetails == null){
            throw new BadCredentialsException("Usuario no encontrado");
        }
        // Verificar si el usuario está activo
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if(usuario.isPresent()){
            Usuario user = usuario.get();
            if (!user.getIsEnable()) {
                throw new BadCredentialsException("El usuario está inactivo");
            }
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public void sendEmailResetPass(EmailValuesDTO dto, Usuario usuario) throws MessagingException {
        String link = urlFront + dto.getTokenPassword();
        emailService.sendResetPasswordEmail(dto.getMailTo(), usuario.getNombre(), link);
    }
}
