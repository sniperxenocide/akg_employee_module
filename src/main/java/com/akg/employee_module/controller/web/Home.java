package com.akg.employee_module.controller.web;

import com.akg.employee_module.API;
import com.akg.employee_module.service.CommonMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
public class Home {
    @Autowired
    CommonMethodService commonMethodService;

    @RequestMapping(API.homePageAPI)
    public String homePage(HttpServletRequest request){
        if (commonMethodService.isValidAdmin(request)) return "home";
        return "error";
    }
}
