package com.akg.employee_module.model;

import com.akg.employee_module.TABLE;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor @ToString
@Table(name = TABLE.BROADCAST_HEADER)
public class BroadcastHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String messageBody;
    private String statusMessage;
    @CreationTimestamp
    private LocalDateTime createTime;
    private Long createdBy;

    public BroadcastHeader(String messageBody, Long createdBy) {
        this.messageBody = messageBody;
        this.createdBy = createdBy;
    }
}
