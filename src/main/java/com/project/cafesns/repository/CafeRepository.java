package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Cafe;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Cafe findByCafeId(Long cafeId);
}
