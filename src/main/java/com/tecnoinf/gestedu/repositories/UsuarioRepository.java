package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findByCi(String ci);

    Optional<Usuario> findByEmail(String email);

    Boolean existsByCi(String ci);

    Boolean existsByEmail(String email);

}
