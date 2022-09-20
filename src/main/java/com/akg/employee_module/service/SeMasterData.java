package com.akg.employee_module.service;

import com.akg.employee_module.enums.Decision;
import com.akg.employee_module.request_response.Response;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SeMasterData {

    private final SeBusiness seBusiness;
    private final SeCompany seCompany;
    private final SeDepartment seDepartment;
    private final SeDesignation seDesignation;
    private final SeWorkLocation seWorkLocation;
    private final SeBroadcastMedia seBroadcastMedia;

    public SeMasterData(SeBusiness seBusiness, SeCompany seCompany, SeDepartment seDepartment, SeDesignation seDesignation, SeWorkLocation seWorkLocation, SeBroadcastMedia seBroadcastMedia, SeEmployee seEmployee) {
        this.seBusiness = seBusiness;
        this.seCompany = seCompany;
        this.seDepartment = seDepartment;
        this.seDesignation = seDesignation;
        this.seWorkLocation = seWorkLocation;
        this.seBroadcastMedia = seBroadcastMedia;
    }

    public Response getAll(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("business",seBusiness.getAll());
        map.put("company",seCompany.getAll());
        map.put("department",seDepartment.getAll());
        map.put("designation",seDesignation.getAll());
        map.put("work_location",seWorkLocation.getAll());
        map.put("broadcast_media",seBroadcastMedia.getAllActive(Decision.YES));
        return new Response(true,"Success",map);
    }
}
