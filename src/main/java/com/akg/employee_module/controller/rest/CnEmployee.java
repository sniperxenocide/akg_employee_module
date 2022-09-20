package com.akg.employee_module.controller.rest;

import com.akg.employee_module.API;
import com.akg.employee_module.model.Employee;
import com.akg.employee_module.service.SeEmployee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class CnEmployee {
    private final SeEmployee service;

    public CnEmployee(SeEmployee service) {
        this.service = service;
    }

    @RequestMapping(value = API.getAllEmployeeAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getAll(
            @RequestParam(required = false,defaultValue = "%") String businessId,
            @RequestParam(required = false,defaultValue = "%") String companyId,
            @RequestParam(required = false,defaultValue = "%") String departmentId,
            @RequestParam(required = false,defaultValue = "%") String designationId,
            @RequestParam(required = false,defaultValue = "%") String workLocationId,
            @RequestParam(required = false,defaultValue = "%") String isVerified,
            @RequestParam(required = false,defaultValue = "%") String isActive,
            @RequestParam int page
    ){
        return new ResponseEntity<>(service.getAllFiltered(
                businessId, companyId, departmentId,
                designationId, workLocationId, isVerified,isActive,page
        ),
                HttpStatus.OK);
    }

    @RequestMapping(value = API.getAllQueriedEmployeeAPI,method = RequestMethod.POST)
    public ResponseEntity<Object> getAllQueried(@RequestBody Map<String, List<Long>> body){
        return new ResponseEntity<>(service.getAllQueried(body),HttpStatus.OK);
    }

    @RequestMapping(value = API.getOneEmployeeAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getOne(@PathVariable Long id){
        return new ResponseEntity<>(service.getOne(id), HttpStatus.OK);
    }

    @RequestMapping(value = API.getOneEmployeeByNidAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getOneByNid(@PathVariable String nid){
        return new ResponseEntity<>(service.getByNid(nid), HttpStatus.OK);
    }

    @RequestMapping(value = API.createEmployeeAPI,method = RequestMethod.POST)
    public ResponseEntity<Object> create(HttpServletRequest request, @RequestBody Employee employee){
        return new ResponseEntity<>(service.create(request,employee), HttpStatus.OK);
    }

    @RequestMapping(value = API.updateEmployeeAPI,method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@PathVariable Long id,@RequestBody Employee employee,HttpServletRequest request){
        return new ResponseEntity<>(service.update(id,employee,request), HttpStatus.OK);
    }

    @RequestMapping(value = API.deleteEmployeeAPI,method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable Long id,HttpServletRequest request){
        return new ResponseEntity<>(service.delete(id,request),HttpStatus.OK);
    }

    @RequestMapping(value = API.getOneEmployeeByOracleIdAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getOneByOracleId(@PathVariable String oracleId){
        return new ResponseEntity<>(service.getByOracleId(oracleId),HttpStatus.OK);
    }

    @RequestMapping(value = API.getOneEmployeeByEmployeeNumberAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getOneByEmployeeNumber(@PathVariable String empNum){
        return new ResponseEntity<>(service.getByEmployeeNumber(empNum),HttpStatus.OK);
    }

    @RequestMapping(value = API.getOneEmployeeByBirthCertificateAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getOneByBirthCertificate(@PathVariable String certNum){
        return new ResponseEntity<>(service.getByBirthCertificate(certNum),HttpStatus.OK);
    }

    @RequestMapping(value = API.getAllEmployeeByNameAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getAllByName(@PathVariable String name){
        return new ResponseEntity<>(service.getAllByName(name),HttpStatus.OK);
    }
}
