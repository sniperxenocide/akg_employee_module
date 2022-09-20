package com.akg.employee_module.service;

import com.akg.employee_module.repository.ReBroadcastDetail;
import org.springframework.stereotype.Service;

@Service
public class SeBroadcastDetail {
    private final ReBroadcastDetail repository;

    public SeBroadcastDetail(ReBroadcastDetail repository) {
        this.repository = repository;
    }
}
