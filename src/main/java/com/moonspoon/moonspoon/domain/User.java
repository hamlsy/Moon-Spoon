package com.moonspoon.moonspoon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String userId;
    private String name;
    private String password;

    private UserRole role;

    @Builder
    public User(String userId, String name, String password, UserRole role){
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.role = role;
    }
}
