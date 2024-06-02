package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.certificado.CertificadoDTO;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Usuario;

public interface CertificadoService {
    CertificadoDTO generarCertificado(String nombreCarrera, Estudiante estudiante);
    CertificadoDTO validarCertificado(String codigoValidacion);
}
