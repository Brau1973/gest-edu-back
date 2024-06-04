package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Titulo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TituloRepository extends JpaRepository<Titulo, Long> {
}
