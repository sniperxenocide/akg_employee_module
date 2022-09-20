package com.akg.employee_module.controller.web;

import com.akg.employee_module.API;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Employee {

    @RequestMapping(API.allEmployeePageAPI)
    public String getReportPage(){
        return "employee";
    }
}
