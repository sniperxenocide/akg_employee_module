package com.akg.employee_module.model;

import com.akg.employee_module.TABLE;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = TABLE.BUSINESS)
public class Business implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Business() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
