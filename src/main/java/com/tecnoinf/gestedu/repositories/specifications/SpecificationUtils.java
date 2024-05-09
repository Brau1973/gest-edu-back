package com.tecnoinf.gestedu.repositories.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class SpecificationUtils {

    public static Predicate likeIgnoreCase(CriteriaBuilder criteriaBuilder, Expression<String> path, String value) {
        return criteriaBuilder.like(criteriaBuilder.lower(path), "%" + value.toLowerCase() + "%");
    }
}
