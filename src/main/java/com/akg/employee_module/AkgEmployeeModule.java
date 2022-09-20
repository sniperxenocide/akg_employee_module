package com.akg.employee_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class AkgEmployeeModule {
    public static void main(String[] args) {
        SpringApplication.run(AkgEmployeeModule.class, args);
    }
}
