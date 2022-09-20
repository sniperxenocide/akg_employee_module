package com.akg.employee_module.controller.rest;

import com.akg.employee_module.API;
import com.akg.employee_module.service.SeMasterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CnMasterData {

    private final SeMasterData service;


    public CnMasterData(SeMasterData service) {
        this.service = service;
    }

    @RequestMapping(value = API.getAllMasterData)
    public ResponseEntity<Object> getAll(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }
}
