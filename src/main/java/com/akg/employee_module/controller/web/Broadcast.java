package com.akg.employee_module.controller.web;

import com.akg.employee_module.API;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Broadcast {
    @RequestMapping(value = API.broadcastPageAPI)
    public String getBroadcastPage(){return "broadcast";}
}
