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
// 조회할 때 항상 삭제 안 된 것만 보이도록
@Where(clause = "deleted_at IS NULL")
// 진짜 삭제는 아니고 삭제한척하려고.. 삭제 시간만 적어서 null 이 아니게 설정하는거!
@SQLDelete(sql = "UPDATE post_images SET deleted_at = NOW() WHERE id = ?")
@Table(name = "post_images")
public class PostImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "object_key")
    private String objectKey;

    @Column(nullable = false, name = "original_name")
    private String originalName;

    @Column(nullable = false, name = "mime_type")
    private String mimeType;

    @Column(nullable = false, name = "size_bytes")
    private int sizeBytes;

    /*
    @Column(nullable = false, name = "file_path")
    private String filePath;
    */

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    // NULL = 살아있음, 값 있음 = 삭제됨
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts posts;

    @Builder
    public PostImages(String objectKey, String originalName, String mimeType, int sizeBytes, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, Users users, Posts posts) {
        this.objectKey = objectKey;
        this.originalName = originalName;
        this.mimeType = mimeType;
        this.sizeBytes = sizeBytes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.users = users;
        this.posts = posts;
    }
}
