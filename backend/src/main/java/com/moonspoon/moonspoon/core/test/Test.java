package com.moonspoon.moonspoon.core.test;

import com.moonspoon.moonspoon.core.sharedWorkbook.SharedWorkbook;
import com.moonspoon.moonspoon.core.testAnswer.TestAnswer;
import com.moonspoon.moonspoon.core.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    @Builder
    public Test(User user, SharedWorkbook sharedWorkbook, String name, LocalDateTime testDate, double score) {
        setUser(user);
        setSharedWorkbook(sharedWorkbook);
        this.name = name;
        this.testDate = testDate;
    }

    public void setUser(User user){
        this.user = user;
        user.getTests().add(this);
    }

    public void setSharedWorkbook(SharedWorkbook sharedWorkbook){
        this.sharedWorkbook = sharedWorkbook;
        sharedWorkbook.getTests().add(this);
    }

}
