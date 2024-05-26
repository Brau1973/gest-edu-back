package com.tecnoinf.gestedu.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

import com.tecnoinf.gestedu.dtos.TipoUsuario;
import com.tecnoinf.gestedu.dtos.usuario.AuthResponse;
import com.tecnoinf.gestedu.dtos.usuario.CrearUsuarioDTO;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Administrador;
import com.tecnoinf.gestedu.models.Coordinador;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.services.implementations.UserDetailsServiceImpl;
import com.tecnoinf.gestedu.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

class UsuarioDetailsServiceImplTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    AuthResponse authResponse;

    @InjectMocks
    UserDetailsServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUsuario_Success_WithValidInput() {
        // Arrange
        CrearUsuarioDTO createUsuarioDTO = new CrearUsuarioDTO(
                "1234567", "nombre", "apellido", "email@email", "password",
                "telefono", "domicilio", new Date(), TipoUsuario.COORDINADOR);

        Coordinador usuario = new Coordinador();

        when(passwordEncoder.encode(createUsuarioDTO.getPassword())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Coordinador.class))).thenAnswer(invocation -> {
            Coordinador savedUsuario = invocation.getArgument(0);
            savedUsuario.setId(1L); // Assuming there's an ID field
            return savedUsuario;
        });
        when(jwtUtils.crearToken(any())).thenReturn("jwtToken");

        // Act
        AuthResponse result = usuarioService.registrarUsuario(createUsuarioDTO);

        // Assert
        assertEquals(createUsuarioDTO.getEmail(), result.getEmail());

        // Capture the saved Coordinador
        ArgumentCaptor<Coordinador> coordinadorCaptor = forClass(Coordinador.class);
        verify(usuarioRepository).save(coordinadorCaptor.capture());
        Coordinador savedUsuario = coordinadorCaptor.getValue();

        // Verify the fields of savedUsuario
        assertEquals(createUsuarioDTO.getCi(), savedUsuario.getCi());
        assertEquals(createUsuarioDTO.getNombre(), savedUsuario.getNombre());
        assertEquals(createUsuarioDTO.getApellido(), savedUsuario.getApellido());
        assertEquals(createUsuarioDTO.getEmail(), savedUsuario.getEmail());
        assertEquals("encodedPassword", savedUsuario.getPassword());
        assertEquals(createUsuarioDTO.getTelefono(), savedUsuario.getTelefono());
        assertEquals(createUsuarioDTO.getDomicilio(), savedUsuario.getDomicilio());
        assertEquals(createUsuarioDTO.getFechaNac(), savedUsuario.getFechaNac());
        assertTrue(savedUsuario.getIsEnable());
        assertTrue(savedUsuario.getAccountNonExpired());
        assertTrue(savedUsuario.getAccountNonLocked());
        assertTrue(savedUsuario.getCredentialsNonExpired());

        verify(passwordEncoder).encode(createUsuarioDTO.getPassword());
        verify(jwtUtils).crearToken(any());
    }

    @Test
    void testCreateUsuario_Failure_WithEmailAlreadyExists() {
        // Arrange
        CrearUsuarioDTO createUsuarioDTO = new CrearUsuarioDTO(
                "ci", "nombre", "apellido", "email@email", "password",
                "telefono", "domicilio", new Date(), TipoUsuario.ADMINISTRADOR);

        Administrador usuario = new Administrador();
        usuario.setCi(createUsuarioDTO.getCi());
        usuario.setNombre(createUsuarioDTO.getNombre());
        usuario.setApellido(createUsuarioDTO.getApellido());
        usuario.setEmail(createUsuarioDTO.getEmail());
        usuario.setPassword(createUsuarioDTO.getPassword());
        usuario.setTelefono(createUsuarioDTO.getTelefono());
        usuario.setDomicilio(createUsuarioDTO.getDomicilio());
        usuario.setFechaNac(createUsuarioDTO.getFechaNac());
        usuario.setIsEnable(true);
        usuario.setAccountNonExpired(true);
        usuario.setAccountNonLocked(true);
        usuario.setCredentialsNonExpired(true);

        when(passwordEncoder.encode(createUsuarioDTO.getPassword())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Administrador.class))).thenThrow(new UniqueFieldException("email"));

        // Act & Assert
        UniqueFieldException exception = assertThrows(UniqueFieldException.class, () -> {
            usuarioService.registrarUsuario(createUsuarioDTO);
        });
        assertEquals("email", exception.getMessage());

        // Verify interactions
        verify(usuarioRepository).save(any(Administrador.class));
        verify(passwordEncoder).encode(createUsuarioDTO.getPassword());
        verify(jwtUtils, never()).crearToken(any());
    }

}