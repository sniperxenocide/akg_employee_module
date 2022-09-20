package com.akg.employee_module.service;

import com.akg.employee_module.model.Department;
import com.akg.employee_module.model.Designation;
import com.akg.employee_module.repository.ReDesignation;
import com.akg.employee_module.request_response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeDesignation {
    private final ReDesignation repository;

    public SeDesignation(ReDesignation repository) {
        this.repository = repository;
    }

    public Response getAll(){
        return new Response(true,"Success", repository.findAll());
    }

    public Response getOne(Long id){
        Optional<Designation> designation = repository.findById(id);
        return designation.map(value -> new Response(true, "Success", value)).orElseGet(() -> new Response(false, "Not Found", null));
    }
}
