package com.akg.employee_module.request_response;

import com.akg.employee_module.enums.Status;
import com.akg.employee_module.model.BroadcastHeader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter @NoArgsConstructor
public class BroadcastStatus {

    @Getter @Setter @NoArgsConstructor
    public class StatusCount{
        private Status status;
        private int count;
    }

    @Getter @Setter @NoArgsConstructor
    public class Media{
        private String name;
        private ArrayList<StatusCount> statusCounts;
    }

    private BroadcastHeader broadcastHeader;
    private ArrayList<Media> broadcastMedias;
    private String createdByUser;
}



