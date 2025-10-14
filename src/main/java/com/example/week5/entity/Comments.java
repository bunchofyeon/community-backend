package com.example.week5.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE comments SET deleted_at = NOW() WHERE id = ?")
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, name = "like_count")
    private Long likeCount;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Posts posts;

    @Builder
    public Comments(String content, Long likeCount, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, Users users, Posts posts) {
        this.content = content;
        this.likeCount = 0L;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.users = users;
        this.posts = posts;
    }

    public void writtenBy(Users writer) {
        this.users = writer;
    }

    public void assignToPost(Posts post) {
        this.posts = post;
        posts.getComments().add(this); // 게시글 - 댓글은 양방향
    }

    // update
    public void update(String content) {
        this.content = content;
    }

}
