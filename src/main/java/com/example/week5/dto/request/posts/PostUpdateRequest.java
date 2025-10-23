package com.example.week5.dto.request.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {

    // PostWriteRequest랑 똑같아서 합칠지 말지 고민...s
    private String title;
    private String content;

    @Builder
    public PostUpdateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
