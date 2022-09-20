package com.akg.employee_module.repository;

import com.akg.employee_module.enums.Decision;
import com.akg.employee_module.model.BroadcastMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;

@Repository
public interface ReBroadcastMedia extends JpaRepository<BroadcastMedia,Long> {
    ArrayList<BroadcastMedia> findAllByMediaVisible(Decision decision);
}
