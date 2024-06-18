package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.certificado.CertificadoDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Certificado;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.repositories.CertificadoRepository;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import com.tecnoinf.gestedu.services.interfaces.CertificadoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificadoServiceImpl implements CertificadoService {

    private final CertificadoRepository certificadoRepository;
    private final ActividadService actividadService;

    public CertificadoServiceImpl(CertificadoRepository certificadoRepository, ActividadService actividadService){
        this.certificadoRepository = certificadoRepository;
        this.actividadService = actividadService;
    }

    @Override
    public CertificadoDTO generarCertificado(String nombreCarrera, Estudiante estudiante){
        Certificado certificado = new Certificado();
        certificado.setCodigoValidacion(UUID.randomUUID().toString());
        certificado.setCarrera(nombreCarrera);
        certificado.setEstudiante(estudiante);
        certificado.setFecha(LocalDate.now());
        certificadoRepository.save(certificado);

        actividadService.registrarActividad(TipoActividad.GENERACION_CERTIFICADO,"Generacion de certificado");

        return new CertificadoDTO(certificado);
    }

    @Override
    public CertificadoDTO validarCertificado(String codigoValidacion) {
        Optional<Certificado> certificado = certificadoRepository.findByCodigoValidacion(codigoValidacion);
        if (certificado.isPresent()) {
            return new CertificadoDTO(certificado.get());
        } else {
            throw new ResourceNotFoundException("Certificado no encontrado o no válido");
        }
    }
}
