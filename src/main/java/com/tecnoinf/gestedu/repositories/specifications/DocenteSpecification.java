package com.tecnoinf.gestedu.repositories.specifications;

import com.tecnoinf.gestedu.models.Docente;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

@AllArgsConstructor
public class DocenteSpecification implements Specification<Docente> {

    private final String documento;
    private final String nombre;
    private final String apellido;

    @Override
    public Predicate toPredicate(@NonNull Root<Docente> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // always true = no filtering
        if (documento != null) {
            predicate = criteriaBuilder.and(predicate, SpecificationUtils.likeIgnoreCase(criteriaBuilder, root.get("documento"), this.documento));
        }
        if (nombre != null) {
            predicate = criteriaBuilder.and(predicate, SpecificationUtils.likeIgnoreCase(criteriaBuilder, root.get("nombre"), this.nombre));
        }
        if (apellido != null) {
            predicate = criteriaBuilder.and(predicate, SpecificationUtils.likeIgnoreCase(criteriaBuilder, root.get("apellido"), this.apellido));
        }
        return predicate;
    }
}
