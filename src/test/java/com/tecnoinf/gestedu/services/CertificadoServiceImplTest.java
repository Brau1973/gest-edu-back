package com.tecnoinf.gestedu.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tecnoinf.gestedu.dtos.certificado.CertificadoDTO;
import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Certificado;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.repositories.CertificadoRepository;
import com.tecnoinf.gestedu.services.implementations.CertificadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class CertificadoServiceImplTest {

    @Mock
    private CertificadoRepository certificadoRepository;

    @InjectMocks
    CertificadoServiceImpl certificadoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerarCertificado() {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Juana");

        String codigoValidacion = UUID.randomUUID().toString();

        Certificado certificado = new Certificado();
        certificado.setCodigoValidacion(codigoValidacion);
        certificado.setCarrera("Ingeniería");
        certificado.setEstudiante(estudiante);
        certificado.setFecha(LocalDate.now());

        when(certificadoRepository.save(any(Certificado.class))).thenAnswer(invocation -> {
            Certificado cert = invocation.getArgument(0);
            cert.setCodigoValidacion(codigoValidacion);
            return cert;
        });

        CertificadoDTO result = certificadoService.generarCertificado("Ingeniería", estudiante);

        assertNotNull(result);
        assertEquals(codigoValidacion, result.getCodigoValidacion());
        assertEquals(certificado.getCarrera(), result.getCarrera());
        assertEquals(certificado.getEstudiante().getNombre(), result.getEstudiante().getNombre());
        assertEquals(certificado.getFecha().toString(), result.getFecha());

        verify(certificadoRepository, times(1)).save(any(Certificado.class));
    }

    @Test
    public void testValidarCertificado() {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Juana");

        String codigoValidacion = UUID.randomUUID().toString();

        Certificado certificado = new Certificado();
        certificado.setCodigoValidacion(codigoValidacion);
        certificado.setCarrera("Ingeniería");
        certificado.setEstudiante(estudiante);
        certificado.setFecha(LocalDate.now());

        when(certificadoRepository.findByCodigoValidacion(codigoValidacion)).thenReturn(Optional.of(certificado));

        CertificadoDTO result = certificadoService.validarCertificado(codigoValidacion);

        assertNotNull(result);
        assertEquals(codigoValidacion, result.getCodigoValidacion());
        assertEquals(certificado.getCarrera(), result.getCarrera());
        assertEquals(certificado.getEstudiante().getNombre(), result.getEstudiante().getNombre());
        assertEquals(certificado.getFecha().toString(), result.getFecha());

        verify(certificadoRepository, times(1)).findByCodigoValidacion(codigoValidacion);
    }

    @Test
    public void testValidarCertificado_CertificadoNoEncontrado() {
        String codigoValidacion = "XYZ789";

        when(certificadoRepository.findByCodigoValidacion(codigoValidacion)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            certificadoService.validarCertificado(codigoValidacion);
        });

        verify(certificadoRepository, times(1)).findByCodigoValidacion(codigoValidacion);
    }


}
