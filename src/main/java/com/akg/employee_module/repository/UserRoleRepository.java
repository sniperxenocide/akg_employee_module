package com.akg.employee_module.repository;

import com.akg.employee_module.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    public Optional<UserRole> findByUserId(int userId);
}
