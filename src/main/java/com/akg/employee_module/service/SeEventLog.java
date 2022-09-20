package com.akg.employee_module.service;

import com.akg.employee_module.model.EventLog;
import com.akg.employee_module.model.User;
import com.akg.employee_module.repository.ReEventLog;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SeEventLog {

    private final ReEventLog repository;
    private final CommonMethodService commonMethodService;

    public SeEventLog(ReEventLog repository, CommonMethodService commonMethodService) {
        this.repository = repository;
        this.commonMethodService = commonMethodService;
    }

    public void createEventLog(HttpServletRequest request,String eventName, String before,String after,String description){
        User user = commonMethodService.getUser(request);
        EventLog eventLog = new EventLog(
                user.getId(),user.getUserName(), request.getRemoteAddr(),
                request.getRequestURI(),eventName,
                before,after,description
        );
        repository.save(eventLog);
    }

    public void createEventLog(User user,HttpServletRequest request,String eventName, String before,String after,String description){
        EventLog eventLog = new EventLog(
                user.getId(),user.getUserName(), request.getRemoteAddr(),
                request.getRequestURI(),eventName,
                before,after,description
        );
        repository.save(eventLog);
    }
}
