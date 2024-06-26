package com.moonspoon.moonspoon.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String username;
    private String name;
    private String password;

    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Workbook> workbooks;

    @Builder
    public User(String username, String name, String password, UserRole role){
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
    }
}
