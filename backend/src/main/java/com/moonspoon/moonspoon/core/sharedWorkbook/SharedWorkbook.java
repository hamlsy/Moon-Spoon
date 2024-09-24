package com.moonspoon.moonspoon.core.sharedWorkbook;

import com.moonspoon.moonspoon.core.comment.Comment;
import com.moonspoon.moonspoon.core.test.Test;
import com.moonspoon.moonspoon.core.workbook.Workbook;
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
public class SharedWorkbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sharedWorkbook_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int testCount;
    private String author;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    private boolean random;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id")
    private Workbook workbook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "sharedWorkbook", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "sharedWorkbook", cascade = CascadeType.REMOVE)
    private List<Test> tests = new ArrayList<>();

    public void setUser(User user){
        this.user = user;
        user.getSharedWorkbooks().add(this);
    }

    public void setWorkbook(Workbook workbook){
        this.workbook = workbook;
        workbook.getSharedWorkbook().add(this);
    }


    public void updateSharedWorkbook(String title, String content, boolean random, LocalDateTime updateDate){
        this.title = title;
        this.content = content;
        this.random = random;
        this.updateDate = updateDate;
    }

    @Builder
    public SharedWorkbook(String title, String content, String author,
                          boolean random, LocalDateTime createDate) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createDate = createDate;
        this.random = random;
    }

}
