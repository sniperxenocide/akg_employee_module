package com.akg.employee_module.controller.rest;

import com.akg.employee_module.API;
import com.akg.employee_module.model.Employee;
import com.akg.employee_module.service.SeOracleEmployee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CnOracleEmployee {
    private final SeOracleEmployee service;

    public CnOracleEmployee(SeOracleEmployee service) {
        this.service = service;
    }

    @RequestMapping(API.getOneOracleEmployeeAPI)
    public ResponseEntity<Object> getEmployeeFromOracle(@PathVariable String oracleId){
        return new ResponseEntity<>(service.getEmployeeFromOracle(oracleId), HttpStatus.OK);
    }

    @PostMapping(API.createEmployeeFromOracleAPI)
    public ResponseEntity<Object> createEmployeeFromOracle(HttpServletRequest request, @RequestBody Employee employee){
        return new ResponseEntity<>(service.createEmployeeFromOracle(request,employee),HttpStatus.OK);
    }
}
