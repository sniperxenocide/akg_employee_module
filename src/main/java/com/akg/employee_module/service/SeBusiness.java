package com.akg.employee_module.service;

import com.akg.employee_module.model.Business;
import com.akg.employee_module.repository.ReBusiness;
import com.akg.employee_module.request_response.Response;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeBusiness {

    private final ReBusiness repository;

    public SeBusiness(ReBusiness repository) {
        this.repository = repository;
    }

    public Response getAll(){
        return new Response(true,"Success", repository.findAll());
    }

    public Response getOne(Long id){
        Optional<Business> business = repository.findById(id);
        return business.map(value -> new Response(true, "Success", value)).orElseGet(() -> new Response(false, "Not Found", null));
    }
}
