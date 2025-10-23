package com.example.week5.dto.response.posts;

import com.example.week5.entity.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
* 리스트
* 게시글 아이디 / 제목 / 닉네임 / 조회수 / 작성일
 */

@Getter
@NoArgsConstructor
public class PostListResponse {

    private Long id;
    private String title;
    private String nickname;
    private Long viewCount;
    private LocalDateTime createdAt;
    // 1. 댓글 수는 redis 배우고 나서 다시...
    // 2. 수정일은 상세 화면에서만 보여줌 // private LocalDateTime updatedAt;

    @Builder
    public PostListResponse(Long id, String title, String nickname, Long viewCount, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
    }

    // Entity -> DTO
    public static PostListResponse fromEntity(Posts posts) {
        return PostListResponse.builder()
                .id(posts.getId())
                .title(posts.getTitle())
                .nickname(posts.getUsers().getNickname())
                .viewCount(posts.getViewCount())
                .createdAt(posts.getCreatedAt())
                .build();
    }

}
