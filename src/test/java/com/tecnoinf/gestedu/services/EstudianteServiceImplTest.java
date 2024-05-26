package com.tecnoinf.gestedu.services;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EstudianteServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EstudianteServiceImpl estudianteService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerEstudiantePorCi() {
        String ci = "123456";
        Estudiante estudiante = new Estudiante();
        estudiante.setCi(ci);

        when(usuarioRepository.findByCi(ci)).thenReturn(Optional.of(estudiante));

        Optional<BasicInfoUsuarioDTO> result = estudianteService.obtenerEstudiantePorCi(ci);

        assertEquals(ci, result.get().getCi());
    }

    @Test
    public void testObtenerEstudiantePorCiNotFound() {
        String ci = "123456";

        when(usuarioRepository.findByCi(ci)).thenReturn(Optional.empty());

        Optional<BasicInfoUsuarioDTO> result = estudianteService.obtenerEstudiantePorCi(ci);

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testObtenerEstudiantes() {
        Estudiante estudiante1 = new Estudiante();
        estudiante1.setCi("123456");
        Estudiante estudiante2 = new Estudiante();
        estudiante2.setCi("789012");

        Pageable pageable = PageRequest.of(0, 2);
        when(usuarioRepository.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(estudiante1, estudiante2), pageable, 2));

        Page<BasicInfoUsuarioDTO> result = estudianteService.obtenerEstudiantes(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("123456", result.getContent().get(0).getCi());
        assertEquals("789012", result.getContent().get(1).getCi());
    }

}