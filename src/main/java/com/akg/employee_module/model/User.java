package com.akg.employee_module.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.akg.employee_module.TABLE;
import javax.persistence.*;

@Entity
@Table(name = TABLE.USER)
public class User {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name="user_name",unique=true)
    private String userName;

    @JsonIgnore
    @Column(name="password")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
