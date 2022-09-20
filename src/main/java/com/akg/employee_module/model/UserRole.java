package com.akg.employee_module.model;

import com.akg.employee_module.TABLE;

import javax.persistence.*;

@Entity
@Table(name = TABLE.USER_ROLE)
public class UserRole {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "user_id")
    public Integer userId;

    @Column(name = "role_id")
    public Integer roleId;
}
