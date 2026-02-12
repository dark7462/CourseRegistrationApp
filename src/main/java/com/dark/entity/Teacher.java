package com.dark.entity;

import jakarta.persistence.*;


@Entity
public class Teacher {
    @Id
    @Column(name = "emp_id")
    private String employmentId; // Admin ID

    private String name;
    private String password;

    public Teacher(String employmentId, String name, String password) {
        this.employmentId = employmentId;
        this.name = name;
        this.password = password;
    }
    public Teacher() {

    }

    public String getEmploymentId() {
        return employmentId;
    }

    public void setEmploymentId(String employmentId) {
        this.employmentId = employmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}