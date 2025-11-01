package com.example.week5.entity;

import com.example.week5.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter @NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE files SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Files extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "origin_name")
    private String originName; // 업로드 당시 원본 파일명

    @Column(nullable = false, name = "file_type")
    private String fileType;

    @Column(nullable = false, name = "file_size")
    private Long fileSize;

    @Column(nullable = false, unique = true, name = "storage_key")
    private String storageKey; // 로컬 경로나 S3 key

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 게시글 있어야 파일도 있으니까 optional = false
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts posts;


    // public void setPosts(Posts posts)
    public void setMappingPosts(Posts posts) {
        this.posts = posts;
    }

}
