package com.akg.employee_module.model;

import com.akg.employee_module.TABLE;
import com.akg.employee_module.enums.Decision;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
@JsonIgnoreProperties(value = { "code","apiBaseUrl","apiUserId","apiPassword","apiSenderId", "apiSenderName",
        "apiTokenKey","apiRequestMethod", "description","mediaVisible","recipientContactColumn","broadcastEnabled" })
@Table(name = TABLE.BROADCAST_MEDIA)
public class BroadcastMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String apiBaseUrl;
    private String apiUserId;
    private String apiPassword;
    private String apiSenderId;
    private String apiSenderName;
    private String apiTokenKey;
    private String apiRequestMethod;
    private String description;
    @Enumerated(EnumType.STRING)
    private Decision mediaVisible;
    private String recipientContactColumn;
    @Enumerated(EnumType.STRING)
    private Decision broadcastEnabled;
}
