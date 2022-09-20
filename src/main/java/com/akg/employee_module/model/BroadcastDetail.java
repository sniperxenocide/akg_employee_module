package com.akg.employee_module.model;

import com.akg.employee_module.TABLE;
import com.akg.employee_module.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor @ToString
@Table(name = TABLE.BROADCAST_DETAIL)
public class BroadcastDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long broadcastHeaderId;
    private Long mediaId;
    private String mediaName;
    private String mediaCode;
    private String messageBody;
    private String recipientContact;
    @Enumerated(EnumType.STRING) private Status status=Status.QUEUE;
    @CreationTimestamp private LocalDateTime createTime;
    private String apiResponse;
    private LocalDateTime apiResponseTime;
    private Long employeeId;
    private Long companyId;
    private Long departmentId;
    private Long designationId;
    private Long workLocationId;
    private String employeeName;
    private String company;
    private String department;
    private String designation;
    private String workLocation;

    public BroadcastDetail(Long broadcastHeaderId, Long mediaId, String mediaName, String mediaCode, String messageBody, String recipientContact, Long employeeId, Long companyId, Long departmentId, Long designationId, Long workLocationId, String employeeName, String company, String department, String designation, String workLocation) {
        this.broadcastHeaderId = broadcastHeaderId;
        this.mediaId = mediaId;
        this.mediaName = mediaName;
        this.mediaCode = mediaCode;
        this.messageBody = messageBody;
        this.recipientContact = recipientContact;
        this.employeeId = employeeId;
        this.companyId = companyId;
        this.departmentId = departmentId;
        this.designationId = designationId;
        this.workLocationId = workLocationId;
        this.employeeName = employeeName;
        this.company = company;
        this.department = department;
        this.designation = designation;
        this.workLocation = workLocation;
    }
}

