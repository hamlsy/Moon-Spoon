package com.moonspoon.moonspoon.user;

import com.moonspoon.moonspoon.workbook.Workbook;
import com.moonspoon.moonspoon.notice.Notice;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String name;

    private String password;

    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Workbook> workbooks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Notice> notices = new ArrayList<>();

    @Builder
    public User(String username, String name, String password, UserRole role){
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
    }


}
