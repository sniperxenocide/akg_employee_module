package com.akg.employee_module.controller.rest;

import com.akg.employee_module.API;
import com.akg.employee_module.service.SeDepartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CnDepartment {
    private final SeDepartment service;

    public CnDepartment(SeDepartment service) {
        this.service = service;
    }

    @RequestMapping(value = API.getAllDepartmentAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getAll(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = API.getOneDepartmentAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getOne(@PathVariable Long id){
        return new ResponseEntity<>(service.getOne(id), HttpStatus.OK);
    }
}
