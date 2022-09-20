package com.akg.employee_module.repository;

import com.akg.employee_module.model.BroadcastHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReBroadcastHeader extends JpaRepository<BroadcastHeader,Long> {
}
