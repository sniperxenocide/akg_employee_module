package com.akg.employee_module.repository;

import com.akg.employee_module.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReDesignation extends JpaRepository<Designation,Long> {
    Optional<Designation> findByName(String name);
}
