package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {
    Cafe findByCafename(String businessname);

    Cafe findByUser(User user);

    boolean existsByAddressAndCafename(String Address,String Cafename);

    List<Cafe> findAllByAddressContaining(String region);

    List<Cafe> findAllByCafenameContaining(String cafename);
}
