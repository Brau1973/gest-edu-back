package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.AsignaturaNeo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignaturaNeoRepository extends Neo4jRepository<AsignaturaNeo, Long> {
    public Optional<AsignaturaNeo> findById(Long id);
    public Optional<AsignaturaNeo> findByNombre(String nombre);
    public List<AsignaturaNeo> findAll();
}