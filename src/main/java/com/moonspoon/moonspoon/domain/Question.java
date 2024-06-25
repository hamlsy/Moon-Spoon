package com.moonspoon.moonspoon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue
    @Column(name = "question_id")
    private Long id;

    private String solution;
    private String content;
    private Double accuracy;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    private int correctCount;
    private int incorrectCount;

    @Builder
    public Question(String solution, String content, Double accuracy, LocalDateTime createDate, LocalDateTime updateDate, int correctCount, int incorrectCount) {
        this.solution = solution;
        this.content = content;
        this.accuracy = accuracy;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.correctCount = correctCount;
        this.incorrectCount = incorrectCount;
    }
}
