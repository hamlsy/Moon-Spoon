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
public class Workbook {
    @Id
    @GeneratedValue
    @Column(name="workbook_id")
    private Long id;

    private String title;
    private String content;
    private String author;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public Workbook(String title, String content, String author, LocalDateTime createDate, LocalDateTime updateDate) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
