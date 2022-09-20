package com.akg.employee_module.model;
import com.akg.employee_module.TABLE;
import javax.persistence.*;

@Entity
@Table(name = TABLE.WORK_LOCATION)
public class WorkLocation {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public WorkLocation(){}

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
