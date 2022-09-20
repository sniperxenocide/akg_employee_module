package com.akg.employee_module.controller.web;

import com.akg.employee_module.API;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BroadcastStatus {

    @RequestMapping(value = API.broadcastStatusPageAPI)
    public String getBroadcastStatusPage(){return "broadcast_status";}

}
