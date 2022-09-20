package com.akg.employee_module.controller.rest;

import com.akg.employee_module.API;
import com.akg.employee_module.service.SeWorkLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CnWorkLocation {
    private final SeWorkLocation service;

    public CnWorkLocation(SeWorkLocation service) {
        this.service = service;
    }

    @RequestMapping(value = API.getAllWorkLocationAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getAll(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = API.getOneWorkLocationAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getOne(@PathVariable Long id){
        return new ResponseEntity<>(service.getOne(id), HttpStatus.OK);
    }
}
