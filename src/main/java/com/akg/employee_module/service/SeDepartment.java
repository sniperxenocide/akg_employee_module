package com.akg.employee_module.service;

import com.akg.employee_module.model.Business;
import com.akg.employee_module.model.Department;
import com.akg.employee_module.repository.ReDepartment;
import com.akg.employee_module.request_response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeDepartment {

    private final ReDepartment repository;

    public SeDepartment(ReDepartment repository) {
        this.repository = repository;
    }

    public Response getAll(){
        return new Response(true,"Success", repository.findAll());
    }

    public Response getOne(Long id){
        Optional<Department> department = repository.findById(id);
        return department.map(value -> new Response(true, "Success", value)).orElseGet(() -> new Response(false, "Not Found", null));
    }
}
