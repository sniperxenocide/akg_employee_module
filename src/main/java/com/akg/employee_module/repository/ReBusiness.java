package com.akg.employee_module.repository;

import com.akg.employee_module.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReBusiness extends JpaRepository<Business,Long> {
    Optional<Business> findByName(String name);
}
