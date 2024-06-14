package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByEstudianteEmail(String name);
}
