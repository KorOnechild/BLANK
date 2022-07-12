package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {
    boolean existsCafeByAddressAndCafename(String address, String cafename);
    Cafe findByCafename(String businessname);
    boolean existsByAddressAndCafename(String Address,String Cafename);

    boolean existsByUser(User user);
    Cafe findByUser(User user);

    List<Cafe> findAllByAddressContaining(String region);

    List<Cafe> findAllByCafenameContaining(String cafename);
}
