package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TramiteRepository extends JpaRepository<Tramite, Long>, JpaSpecificationExecutor<Carrera> {
}

