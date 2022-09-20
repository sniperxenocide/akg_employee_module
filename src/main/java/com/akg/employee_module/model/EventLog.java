package com.akg.employee_module.model;

import com.akg.employee_module.TABLE;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = TABLE.EVENT_LOG)
public class EventLog {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "url")
    private String url;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "before_event")
    private String beforeEvent;

    @Column(name = "after_event")
    private String afterEvent;

    @Column(name = "description")
    private String description;

    @Column(name = "event_time")
    @CreationTimestamp
    private LocalDateTime eventTime;

    public EventLog(){}

    public EventLog(Long userId, String username, String ipAddress, String url, String eventName, String beforeEvent, String afterEvent, String description) {
        this.userId = userId;
        this.username = username;
        this.ipAddress = ipAddress;
        this.url = url;
        this.eventName = eventName;
        this.beforeEvent = beforeEvent;
        this.afterEvent = afterEvent;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getBeforeEvent() {
        return beforeEvent;
    }

    public void setBeforeEvent(String beforeEvent) {
        this.beforeEvent = beforeEvent;
    }

    public String getAfterEvent() {
        return afterEvent;
    }

    public void setAfterEvent(String afterEvent) {
        this.afterEvent = afterEvent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
