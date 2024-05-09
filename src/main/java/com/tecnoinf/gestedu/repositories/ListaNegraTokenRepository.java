package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.ListaNegraToken;
import org.springframework.data.repository.CrudRepository;

public interface ListaNegraTokenRepository extends CrudRepository<ListaNegraToken, String> {

    ListaNegraToken findByToken(String token);
}
