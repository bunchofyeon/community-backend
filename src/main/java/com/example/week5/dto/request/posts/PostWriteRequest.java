package com.example.week5.dto.request.posts;

import com.example.week5.entity.Posts;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostWriteRequest {


    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max=26, message = "제목은 최대 26자까지 가능합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
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
