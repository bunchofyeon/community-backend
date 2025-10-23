package com.example.week5.dto.request.posts;

import com.example.week5.entity.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostWriteRequest {

    // 글 올릴 때 어떤 정보를 받을건지...
    // 파일은...??
    private String title;
    private String content;

    @Builder
    public PostWriteRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // DTO → Entity
    public static Posts ofEntity(PostWriteRequest dto) {
        return Posts.builder()
                .title(dto.title)
                .content(dto.content)
                .build();
    }
}
