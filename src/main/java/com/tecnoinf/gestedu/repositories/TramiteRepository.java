package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TramiteRepository extends JpaRepository<Carrera, Long>, JpaSpecificationExecutor<Carrera> {
}

