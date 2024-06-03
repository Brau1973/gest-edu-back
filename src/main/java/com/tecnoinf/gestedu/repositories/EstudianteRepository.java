package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Estudiante;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends UsuarioRepository{
    @Query("SELECT e FROM Estudiante e WHERE e.email = ?1")
    Optional<Estudiante> findEstudianteByEmail(String email);
}
