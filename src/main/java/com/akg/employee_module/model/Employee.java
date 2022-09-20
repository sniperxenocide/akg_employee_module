package com.akg.employee_module.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Employee implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nid;
    private LocalDateTime dateOfBirth;
    private String officialPhone;
    private String personalPhone;
    private String emergencyContact;
    private String postalAddress;
    private String email;
    private LocalDate marriageDate;
    private Long businessId;
    private Long companyId;
    private Long departmentId;
    private Long designationId;
    private Long workLocationId;
    private String oracleId;
    private LocalDateTime joiningDate;
    private String employeeNumber;
    private String birthCertificate;
    private String hasSmartphone;
    private String whatsappNumber;
    private String imoNumber;
    private String viberNumber;
    private String fbProfileLink;
    private String isVerified = "NO";
    @CreationTimestamp
    private LocalDateTime createDate;
    private Long createdBy;
    private String isActive = "YES";

    public void updateEmployee(Employee e){
        this.name = e.name;
        this.nid = e.nid;
        this.dateOfBirth = e.dateOfBirth;
        this.officialPhone = e.officialPhone;
        this.personalPhone = e.personalPhone;
        this.emergencyContact = e.emergencyContact;
        this.postalAddress = e.postalAddress;
        this.email = e.email;
        this.marriageDate = e.marriageDate;
        this.businessId = e.businessId;
        this.companyId = e.companyId;
        this.departmentId = e.departmentId;
        this.designationId = e.designationId;
        this.workLocationId = e.workLocationId;
        this.oracleId = e.oracleId;
        this.joiningDate = e.joiningDate;
        this.employeeNumber = e.employeeNumber;
        this.birthCertificate = e.birthCertificate;
        this.hasSmartphone = e.hasSmartphone;
        this.whatsappNumber = e.whatsappNumber;
        this.imoNumber = e.imoNumber;
        this.viberNumber = e.viberNumber;
        this.fbProfileLink = e.fbProfileLink;
        this.isVerified = e.isVerified;
        this.isActive = e.isActive;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void setMarriageDate(String marriageDate) {
        try {
            this.marriageDate = LocalDate.parse(marriageDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch (Exception e){}
    }

}
