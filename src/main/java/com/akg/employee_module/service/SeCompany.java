package com.akg.employee_module.service;

import com.akg.employee_module.model.Business;
import com.akg.employee_module.model.Company;
import com.akg.employee_module.repository.ReBusiness;
import com.akg.employee_module.repository.ReCompany;
import com.akg.employee_module.request_response.Response;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeCompany {
    private final ReCompany repository;

    public SeCompany(ReCompany repository) {
        this.repository = repository;
    }

    public Response getAll(){
        return new Response(true,"Success", repository.findAll());
    }

    public Response getOne(Long id){
        Optional<Company> company = repository.findById(id);
        return company.map(value -> new Response(true, "Success", value)).orElseGet(() -> new Response(false, "Not Found", null));
    }
}
