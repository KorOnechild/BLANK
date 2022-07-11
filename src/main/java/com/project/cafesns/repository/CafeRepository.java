package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {
    List<Cafe> findAllByAddressContaining(String region);
}
