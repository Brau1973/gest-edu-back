package com.tecnoinf.gestedu.repositories.specifications;

import com.tecnoinf.gestedu.models.Carrera;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

@AllArgsConstructor
public class CarreraSpecification implements Specification<Carrera> {

    private final String nombre;

    @Override
    public Predicate toPredicate(@NonNull Root<Carrera> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        if (nombre == null) {
            return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // always true = no filtering
        } else {
            return SpecificationUtils.likeIgnoreCase(criteriaBuilder, root.get("nombre"), this.nombre);
        }
    }
}