package com.example.week5.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE posts SET deleted_at = NOW() WHERE id = ?")
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 26)
    @Size(min=1, max=26)
    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false, name = "like_count")
    private Long likeCount;

    @Column(nullable = false, name = "comment_count")
    private Long commentCount;

    @Column(nullable = false, name = "view_count")
    private Long viewCount;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    // 게시글 삭제하면 댓글도 삭제 (orphanRemoval = true)
    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments = new ArrayList<>();

    // 게시글 삭제하면 파일도 삭제
    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Files> files = new ArrayList<>();

    @Builder
    public Posts(String title, String content, Long likeCount, Long commentCount, Long viewCount, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, Users users) {
        this.title = title;
        this.content = content;
        this.likeCount = 0L;
        this.commentCount = 0L;
        this.viewCount = 0L;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.users = users;
    }

    // 수정사항 Dirty Checking
    // dto.title 이런식으로 할지 고민
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 사용자 주입 (작성자를 알게 하기 위해)
    // public void setUsers(Users users)
    public void setMappingUsers(Users users) {
        this.users = users;
    }

    /*
     * 조회수 증가
     *     public void upViewCount() {
     *         this.viewCount++;
     *     }
    */

}
