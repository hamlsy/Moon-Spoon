package com.moonspoon.moonspoon.core.comment;

import com.moonspoon.moonspoon.core.sharedWorkbook.SharedWorkbook;
import com.moonspoon.moonspoon.core.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String author;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharedWorkbook_id")
    private SharedWorkbook sharedWorkbook;

    @Builder
    public Comment(Long id, String author, String content, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public void setUser(User user){
        this.user = user;
        user.getComments().add(this);
    }


    public void setSharedWorkbook(SharedWorkbook sharedWorkbook){
        this.sharedWorkbook = sharedWorkbook;
        sharedWorkbook.getComments().add(this);
    }

    public void updateComment(String content, LocalDateTime updateDate){
        this.content = content;
        this.updateDate = updateDate;
    }
}
