package com.akg.employee_module.repository;

import com.akg.employee_module.model.VonageMessagesLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReVonageMessagesLog extends JpaRepository<VonageMessagesLog,Long> {

}
