package com.ram.opsnow.util;

import org.springframework.data.jpa.domain.Specification;

import com.ram.opsnow.tier.entity.Tier;

public class TierSpecification {
    private TierSpecification() {
    }

    public static Specification<Tier> hasTierCode(String tierCode) {
        return (root, query, criteriaBuilder) -> tierCode == null ? null
                : criteriaBuilder.equal(criteriaBuilder.lower(root.get("tierCode")), tierCode.toLowerCase());
    }

    public static Specification<Tier> hasTierName(String tierName) {
        return (root, query, criteriaBuilder) -> tierName == null ? null
                : criteriaBuilder.like(criteriaBuilder.lower(root.get("tierName")), "%" + tierName.toLowerCase() + "%");
    }
}
