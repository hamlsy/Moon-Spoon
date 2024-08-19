package com.moonspoon.moonspoon.workbook;

import com.moonspoon.moonspoon.problem.Problem;
import com.moonspoon.moonspoon.sharedWorkbook.SharedWorkbook;
import com.moonspoon.moonspoon.user.User;
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
public class Workbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="workbook_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;
    private int problemCount;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workbook", fetch=FetchType.LAZY ,cascade = CascadeType.REMOVE)
    private List<Problem> problems = new ArrayList<>();

    @OneToOne(mappedBy = "workbook", fetch=FetchType.LAZY, cascade = CascadeType.REMOVE)
    private SharedWorkbook sharedWorkbook;

    @Builder
    public Workbook(String title, String content, String author, LocalDateTime createDate, LocalDateTime updateDate) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public void update(String title, String content, LocalDateTime updateDate){
        this.title = title;
        this.content = content;
        this.updateDate = updateDate;
    }

    public void setUser(User user){
        this.user = user;
        user.getWorkbooks().add(this);
    }

}
