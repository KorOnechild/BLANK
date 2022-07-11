package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.Menu;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Cafe findByCafeId(Long cafeId);

    List<Menu> findAllByCafeId(Long cafeId);
}
