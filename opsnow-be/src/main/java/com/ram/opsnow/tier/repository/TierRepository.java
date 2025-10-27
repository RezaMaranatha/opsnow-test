package com.ram.opsnow.tier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ram.opsnow.tier.entity.Tier;

public interface TierRepository extends JpaRepository<Tier, String>, JpaSpecificationExecutor<Tier> {

}
