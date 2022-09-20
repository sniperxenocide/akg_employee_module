package com.akg.employee_module.repository;

import com.akg.employee_module.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReDepartment extends JpaRepository<Department,Long> {
    Optional<Department> findByName(String name);
}
