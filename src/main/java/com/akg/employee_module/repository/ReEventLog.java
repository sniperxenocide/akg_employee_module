package com.akg.employee_module.repository;

import com.akg.employee_module.model.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReEventLog  extends JpaRepository<EventLog,Long> {
}
