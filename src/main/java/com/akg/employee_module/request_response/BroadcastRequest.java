package com.akg.employee_module.request_response;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class BroadcastRequest implements Serializable {
    private String messageBody;
    private ArrayList<Long> broadcastMedias;
    private ArrayList<Long> employeeIds;
}
