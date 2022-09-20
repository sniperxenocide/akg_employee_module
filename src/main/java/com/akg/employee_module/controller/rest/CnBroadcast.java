package com.akg.employee_module.controller.rest;

import com.akg.employee_module.API;
import com.akg.employee_module.model.VonageMessagesLog;
import com.akg.employee_module.repository.ReVonageMessagesLog;
import com.akg.employee_module.request_response.BroadcastRequest;
import com.akg.employee_module.service.SeBroadcast;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class CnBroadcast {
    private final SeBroadcast service;

    public CnBroadcast(SeBroadcast service) {
        this.service = service;
    }

    @RequestMapping(value = API.createBroadcastRequestAPI,method = RequestMethod.POST)
    public ResponseEntity<Object> createBroadcast(@RequestBody BroadcastRequest broadcastRequest, HttpServletRequest request){
        return new ResponseEntity<>(service.createBroadcast(broadcastRequest,request), HttpStatus.OK);
    }

    @RequestMapping(value = API.getAllBroadcastStatusAPI,method = RequestMethod.GET)
    public ResponseEntity<Object> getBroadcastStatus(){
        return new ResponseEntity<>(service.getBroadcastStatus(), HttpStatus.OK);
    }

    @PostMapping(API.vonageMessagesStatusAPI)
    public ResponseEntity<Object> getVonageMessagesStatus(@RequestBody String body){
        service.vonageMessagesStatusLog(body);
        return ResponseEntity.ok("Success");
    }
}
