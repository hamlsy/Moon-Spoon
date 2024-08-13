package com.moonspoon.moonspoon.answer;

import com.moonspoon.moonspoon.problem.Problem;
import com.moonspoon.moonspoon.test.Test;
import jakarta.persistence.*;

@Entity
public class Answer {
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

}
