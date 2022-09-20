package com.akg.employee_module.controller.rest;

import com.akg.employee_module.API;
import com.akg.employee_module.service.SeBusiness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CnBusiness {
    private final SeBusiness service;

    public CnBusiness(SeBusiness service) {
        this.service = service;
    }

    @RequestMapping(value = API.getAllBusinessAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getAll(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = API.getOneBusinessAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getOne(@PathVariable Long id){
        return new ResponseEntity<>(service.getOne(id), HttpStatus.OK);
    }

}
