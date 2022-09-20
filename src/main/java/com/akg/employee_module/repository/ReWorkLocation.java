package com.akg.employee_module.repository;

import com.akg.employee_module.model.WorkLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReWorkLocation extends JpaRepository<WorkLocation,Long> {
    Optional<WorkLocation> findByName(String name);
}
