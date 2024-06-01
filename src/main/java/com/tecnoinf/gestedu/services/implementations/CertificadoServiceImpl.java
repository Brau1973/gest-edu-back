package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.certificado.CertificadoDTO;
import com.tecnoinf.gestedu.models.Certificado;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.CertificadoRepository;
import com.tecnoinf.gestedu.services.interfaces.CertificadoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class CertificadoServiceImpl implements CertificadoService {

    private final CertificadoRepository certificadoRepository;

    public CertificadoServiceImpl(CertificadoRepository certificadoRepository){
        this.certificadoRepository = certificadoRepository;
    }

    @Override
    public CertificadoDTO generarCertificado(String nombreCarrera, Estudiante estudiante){
        Certificado certificado = new Certificado();
        certificado.setCodigoValidacion(UUID.randomUUID().toString());
        certificado.setCarrera(nombreCarrera);
        certificado.setEstudiante(estudiante);
        certificado.setFecha(LocalDate.now());
        certificadoRepository.save(certificado);
        return new CertificadoDTO(certificado);
    }

    @Override
    public CertificadoDTO validarCertificado(String codigoValidacion){
        return null;
    }
}
