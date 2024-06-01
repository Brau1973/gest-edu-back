package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
    Optional<Certificado> findByCodigoValidacion(String codigoValidacion);
}
