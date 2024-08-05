package com.moonspoon.moonspoon.sharedWorkbook;

import com.moonspoon.moonspoon.comment.Comment;
import com.moonspoon.moonspoon.user.User;
import com.moonspoon.moonspoon.workbook.Workbook;
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

    private String title;
    private String content;

    private int testCount;
    private String author;
    private LocalDateTime sharedDate;
    private LocalDateTime updateDate;

    private boolean isRandom;

    @OneToOne
    @JoinColumn(name = "workbook_id")
    private Workbook workbook;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "sharedWorkbook", cascade = CascadeType.REMOVE)
    List<Comment> comments = new ArrayList<>();

    public void setUser(User user){
        this.user = user;
        user.getSharedWorkbooks().add(this);
    }

    public void setWorkbook(Workbook workbook){
        this.workbook = workbook;
        workbook.setSharedWorkbook(this);
    }

    public void updateSharedWorkbook(String title, String content){
        this.title = title;
        this.content = content;
        this.updateDate = LocalDateTime.now();
    }

    @Builder
    public SharedWorkbook(String title, String content, String author,
                          LocalDateTime sharedDate, boolean isRandom) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.sharedDate = sharedDate;
        this.isRandom = isRandom;
    }
}
