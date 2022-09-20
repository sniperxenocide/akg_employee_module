package com.akg.employee_module.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class VonageMessagesLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    private String log;
    @CreationTimestamp private Date logTime;

    public VonageMessagesLog(String log, Date logTime) {
        this.log = log;
        this.logTime = logTime;
    }
}
