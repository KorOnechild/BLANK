package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Register;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegisterRepository extends JpaRepository<Register, Long> {
    List<Register> findAllByPermit(Boolean permit);

    boolean existsByAddressAndCafename(String address, String cafename);
}
