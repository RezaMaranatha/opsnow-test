package com.ram.opsnow.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ram.opsnow.location.entity.Location;

public interface LocationRepository extends JpaRepository<Location, String>, JpaSpecificationExecutor<Location> {

}
