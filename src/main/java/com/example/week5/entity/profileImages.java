package com.example.week5.entity;

import com.example.week5.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
// @Table
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE profileImages SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class profileImages extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "original_name")
    private String originalName;

    @Column(nullable = false, name = "image_type", length = 100)
    private String imageType;

    @Column(nullable = false, name = "file_size")
    private Long fileSize;

    @Column(nullable = false, unique = true, name = "storage_path", length = 512)
    private String storagePath;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 사용자가 있어야 프로필 이미지도 있음
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users users;

    // 연관 편의 메서드
    public void setMappingUser(Users users) {
        this.users = users;
    }
}
