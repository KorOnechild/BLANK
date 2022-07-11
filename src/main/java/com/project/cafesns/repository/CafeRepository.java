package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.Menu;
import com.project.cafesns.model.entitiy.User;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.net.ssl.SSLSession;
import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Cafe findByCafeId(Long cafeId);

    List<Menu> findAllByCafeId(Long cafeId);

    Cafe findByUser(User user);
}
