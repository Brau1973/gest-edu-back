package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.TokenPass;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenPassRepository extends CrudRepository<TokenPass, Long> {

    Optional<TokenPass> findByToken(String token);

}
