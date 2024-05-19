package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.usuario.AuthLoginRequest;
import com.tecnoinf.gestedu.dtos.usuario.AuthResponse;
import com.tecnoinf.gestedu.dtos.usuario.CrearUsuarioDTO;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (usuario instanceof Administrador) {
            authorities.add(new SimpleGrantedAuthority("ROL_ADMINISTRADOR"));
        } else if (usuario instanceof Coordinador) {
            authorities.add(new SimpleGrantedAuthority("ROL_COORDINADOR"));
        } else if (usuario instanceof Funcionario) {
            authorities.add(new SimpleGrantedAuthority("ROL_FUNCIONARIO"));
        } else if (usuario instanceof Estudiante) {
            authorities.add(new SimpleGrantedAuthority("ROL_ESTUDIANTE"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROL_INVITADO"));
        }

        User user = new User(usuario.getEmail(),
                usuario.getPassword(),
                usuario.getIsEnable(),
                usuario.getAccountNonExpired(),
                usuario.getCredentialsNonExpired(),
                usuario.getAccountNonLocked(),
                authorities);
        return user;
    }

    public AuthResponse loginUser(Usuario usuario) {
        return new AuthResponse(usuario.getEmail(), "Usuario logueado", "jwt", true);
    }

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
        UserDetails userDetails = this.loadUserByUsername(email);
        if(userDetails == null){
            throw new BadCredentialsException("Usuario no encontrado");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public AuthResponse registrarUsuario(CrearUsuarioDTO crearUsuarioRequest) {
        Usuario usuario;
        switch(crearUsuarioRequest.getTipoUsuario()){
            case ADMINISTRADOR:
                usuario = new Administrador();
                break;
            case COORDINADOR:
                usuario = new Coordinador();
                break;
            case FUNCIONARIO:
                usuario = new Funcionario();
                break;
            case ESTUDIANTE:
                usuario = new Estudiante();
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuario no válido");
        }
        usuario.setCi(crearUsuarioRequest.getCi());
        usuario.setNombre(crearUsuarioRequest.getNombre());
        usuario.setApellido(crearUsuarioRequest.getApellido());
        usuario.setEmail(crearUsuarioRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(crearUsuarioRequest.getPassword()));
        usuario.setTelefono(crearUsuarioRequest.getTelefono());
        usuario.setDomicilio(crearUsuarioRequest.getDomicilio());
        usuario.setFechaNac(crearUsuarioRequest.getFechaNac());
        usuario.setIsEnable(true);
        usuario.setAccountNonExpired(true);
        usuario.setAccountNonLocked(true);
        usuario.setCredentialsNonExpired(true);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        //generar token de acceso
        ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if (usuarioGuardado instanceof Administrador) {
            authorityList.add(new SimpleGrantedAuthority("ROL_ADMINISTRADOR"));
        } else if (usuarioGuardado instanceof Coordinador) {
            authorityList.add(new SimpleGrantedAuthority("ROL_COORDINADOR"));
        } else if (usuarioGuardado instanceof Funcionario) {
            authorityList.add(new SimpleGrantedAuthority("ROL_FUNCIONARIO"));
        } else if (usuarioGuardado instanceof Estudiante) {
            authorityList.add(new SimpleGrantedAuthority("ROL_ESTUDIANTE"));
        } else {
            authorityList.add(new SimpleGrantedAuthority("ROL_INVITADO"));
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(usuarioGuardado.getEmail(), usuarioGuardado.getPassword(), authorityList);

        String accessToken = jwtUtils.crearToken(authentication);
        AuthResponse authResponse = new AuthResponse(usuarioGuardado.getEmail(), "Usuario registrado", accessToken, true);
        return authResponse;
    }

}
