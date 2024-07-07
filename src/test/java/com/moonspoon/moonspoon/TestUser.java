package com.moonspoon.moonspoon;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_user", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class TestUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @Version
    private Long version;

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

    public Long getVersion() {
        return version;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
