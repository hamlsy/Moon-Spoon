package com.moonspoon.moonspoon.core.notice;

import com.moonspoon.moonspoon.core.user.User;
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
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @Builder
    public Notice(Long id, String title, String content, User user, LocalDateTime createDate, LocalDateTime updateDate, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.author = author;
    }

    //연관관계 메서드
    public void setUser(User user){
        this.user = user;
        user.getNotices().add(this);
    }

    //비즈니스 로직
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

}
