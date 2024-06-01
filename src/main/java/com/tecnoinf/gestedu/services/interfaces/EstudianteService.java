package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.certificado.CertificadoDTO;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EstudianteService {
    Page<BasicInfoCarreraDTO> getCarrerasNoInscripto(String email, Pageable pageable);
    Page<BasicInfoUsuarioDTO> obtenerEstudiantes(Pageable pageable);
    Optional<BasicInfoUsuarioDTO> obtenerEstudiantePorCi(String ci);
    Page<BasicInfoCarreraDTO> getCarrerasInscripto(String email, Pageable pageable);
    Page<AsignaturaDTO> obtenerAsignaturasAExamen(Long carreraId, String email, Pageable pageable);
    CertificadoDTO solicitarCertificado(Long carreraId, String name);
}