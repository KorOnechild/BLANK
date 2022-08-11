package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {
    Cafe findByCafename(String businessname);

    Cafe findByUser(User user);

    boolean existsByAddressContainsAndCafename(String address,String Cafename);

    boolean existsByAddressAndCafename(String address,String Cafename);

    List<Cafe> findAllByAddressContains(String address);

    List<Cafe> findAllByAddressdetailContains(String address);
    
    List<Cafe> findAllByCafenameContains(String cafename);

    @Query("select c from Cafe c join fetch c.postList")
    List<Cafe> findAllCafes();

}
