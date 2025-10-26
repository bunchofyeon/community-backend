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
@SQLDelete(sql = "UPDATE files SET deleted_at = NOW() WHERE id = ?")
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "origin_file_name")
    private String originFileName;

    @Column(nullable = false, name = "file_type")
    private String fileType;

    @Column(nullable = false, name = "file_path")
    private String filePath;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts posts;

    @Builder
    public Files(String filePath, String fileType, String originFileName, Long id, Users users, Posts posts) {
        this.filePath = filePath;
        this.fileType = fileType;
        this.originFileName = originFileName;
        this.id = id;
        this.users = users;
        this.posts = posts;
    }

    // public void setPosts(Posts posts)
    public void setMappingUsers(Posts posts) {
        this.posts = posts;
    }
}
