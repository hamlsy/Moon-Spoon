package com.moonspoon.moonspoon.test;

import com.moonspoon.moonspoon.sharedWorkbook.SharedWorkbook;
import com.moonspoon.moonspoon.testAnswer.TestAnswer;
import com.moonspoon.moonspoon.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharedWorkbook_id")
    private SharedWorkbook sharedWorkbook;


    @OneToMany(mappedBy = "test", cascade = CascadeType.REMOVE)
    private List<TestAnswer> testAnswers = new ArrayList<>();

    private String name;
    private LocalDateTime testDate;
    private double score;


    public void setUser(User user){
        this.user = user;
        user.getTests().add(this);
    }

    public void setSharedWorkbook(SharedWorkbook sharedWorkbook){
        this.sharedWorkbook = sharedWorkbook;
        sharedWorkbook.getTests().add(this);
    }

}
