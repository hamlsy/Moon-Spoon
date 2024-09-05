package com.moonspoon.moonspoon;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
//@Table(name = "test_user", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class TestUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;



    public String getSynName() {
        return synName;
    }

    public void setSynName(String synName) {
        this.synName = synName;
    }

    private String synName;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    private LocalDateTime createDate;

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
