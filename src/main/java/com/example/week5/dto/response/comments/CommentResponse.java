package com.example.week5.dto.response.comments;

import com.example.week5.common.BaseTimeEntity;
import com.example.week5.entity.Comments;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 댓글 조회 응답 DTO
 * 댓글 아이디 / 내용 / 닉네임 / 좋아요 수 / 작성일 / 수정일
 */

@Getter
@NoArgsConstructor
public class CommentResponse extends BaseTimeEntity {

    private Long id;
    private String content;
    private String nickname;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public CommentResponse(Long id, String content, String nickname, Long likeCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Entity -> DTO
    public static CommentResponse fromEntity(Comments comments) {
        return CommentResponse.builder()
                .id(comments.getId())
                .content(comments.getContent())
                .nickname(comments.getUsers().getNickname()) // Users 엔티티 참조
                .likeCount(comments.getLikeCount())
                .createdAt(comments.getCreatedAt())
                .updatedAt(comments.getUpdatedAt())
                .build();
    }
}
