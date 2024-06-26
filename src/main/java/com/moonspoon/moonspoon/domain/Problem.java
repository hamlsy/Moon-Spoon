package com.moonspoon.moonspoon.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Problem {
    @Id
    @GeneratedValue
    @Column(name = "problem_id")
    private Long id;

    private String solution;
    private String question;
    private Double accuracy;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    private int correctCount;
    private int incorrectCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Workbook workbook;

    @Builder
    public Problem(String solution, String question, Double accuracy, LocalDateTime createDate, LocalDateTime updateDate, int correctCount, int incorrectCount) {
        this.solution = solution;
        this.question = question;
        this.accuracy = accuracy;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.correctCount = correctCount;
        this.incorrectCount = incorrectCount;
    }

    public void setWorkbook(Workbook workbook){
        this.workbook = workbook;
        workbook.getProblems().add(this);
    }

    //update
    public void update(String question, String solution, LocalDateTime updateDate){
        this.question = question;
        this.solution = solution;
        this.updateDate = updateDate;
    }

}
