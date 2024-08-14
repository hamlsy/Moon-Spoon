package com.moonspoon.moonspoon.testAnswer;

import com.moonspoon.moonspoon.problem.Problem;
import com.moonspoon.moonspoon.test.Test;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TestAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(columnDefinition = "TEXT")
    private String userAnswer;

    private String name;

    @Builder
    public TestAnswer(String userAnswer, String name) {
        this.userAnswer = userAnswer;
        this.name = name;
    }

    public void setProblem(Problem problem){
        this.problem = problem;
        problem.getTestAnswers().add(this);
    }

    public void setTest(Test test){
        this.test = test;
        test.getTestAnswers().add(this);
    }
}
