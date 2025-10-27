package com.ram.opsnow.util;

import org.springframework.data.jpa.domain.Specification;

import com.ram.opsnow.location.entity.Location;

public class LocationSpecification {
    private LocationSpecification() {
    }

    public static Specification<Location> hasLocationCode(String locationCode) {
        return (root, query, criteriaBuilder) -> locationCode == null ? null
                : criteriaBuilder.equal(criteriaBuilder.lower(root.get("locationCode")), locationCode.toLowerCase());
    }

    public static Specification<Location> hasLocationName(String locationName) {
        return (root, query, criteriaBuilder) -> locationName == null ? null
                : criteriaBuilder.like(criteriaBuilder.lower(root.get("locationName")),
                        "%" + locationName.toLowerCase() + "%");
    }

    public static Specification<Location> hasLocationAddress(String locationAddress) {
        return (root, query, criteriaBuilder) -> locationAddress == null ? null
                : criteriaBuilder.like(criteriaBuilder.lower(root.get("locationAddress")),
                        "%" + locationAddress.toLowerCase() + "%");
    }
}
