package com.akg.employee_module.service;

import com.akg.employee_module.model.Department;
import com.akg.employee_module.model.WorkLocation;
import com.akg.employee_module.repository.ReWorkLocation;
import com.akg.employee_module.request_response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeWorkLocation {
    private final ReWorkLocation repository;

    public SeWorkLocation(ReWorkLocation repository) {
        this.repository = repository;
    }

    public Response getAll(){
        return new Response(true,"Success", repository.findAll());
    }

    public Response getOne(Long id){
        Optional<WorkLocation> workLocation = repository.findById(id);
        return workLocation.map(value -> new Response(true, "Success", value)).orElseGet(() -> new Response(false, "Not Found", null));
    }
}
