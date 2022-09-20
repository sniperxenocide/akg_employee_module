package com.akg.employee_module.repository;

import com.akg.employee_module.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReCompany extends JpaRepository<Company,Long> {
    Optional<Company> findByName(String name);
}
