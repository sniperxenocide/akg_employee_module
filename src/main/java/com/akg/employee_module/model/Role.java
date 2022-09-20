package com.akg.employee_module.model;


import com.akg.employee_module.TABLE;

import javax.persistence.*;

@Entity
@Table(name = TABLE.ROLE)
public class Role {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role")
    private String role;

    public Role(){}

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
