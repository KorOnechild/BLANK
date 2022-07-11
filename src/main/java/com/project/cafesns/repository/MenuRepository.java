package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.print.attribute.Attribute;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Menu findByMenuId(Long menuId);
}
