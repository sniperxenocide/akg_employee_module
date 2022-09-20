package com.akg.employee_module.service;

import com.akg.employee_module.model.BroadcastHeader;
import com.akg.employee_module.repository.ReBroadcastHeader;
import com.akg.employee_module.request_response.Response;
import org.springframework.stereotype.Service;

@Service
public class SeBroadcastHeader {
    private final ReBroadcastHeader repository;

    public SeBroadcastHeader(ReBroadcastHeader repository) {
        this.repository = repository;
    }

    public Response save(BroadcastHeader broadcastHeader){
        try {
            BroadcastHeader bh = repository.save(broadcastHeader);
            return new Response(true,"Success",bh);
        }catch (Exception e){e.printStackTrace();}
        return new Response(false,"Failed");
    }
}
