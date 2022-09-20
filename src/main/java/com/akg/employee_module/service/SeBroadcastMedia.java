package com.akg.employee_module.service;

import com.akg.employee_module.enums.Decision;
import com.akg.employee_module.repository.ReBroadcastMedia;
import com.akg.employee_module.request_response.Response;
import org.springframework.stereotype.Service;

@Service
public class SeBroadcastMedia {
    private final ReBroadcastMedia repository;

    public SeBroadcastMedia(ReBroadcastMedia repository) {
        this.repository = repository;
    }

    public Response getAll(){
        return new Response(true,"Success",repository.findAll());
    }

    public Response getAllActive(Decision decision){
        return new Response(true,"Success",repository.findAllByMediaVisible(decision));
    }
}
