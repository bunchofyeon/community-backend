package com.example.week5.dto.request.comments;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentSearchRequest {

    private String content;
    private String nickname;

    @Builder
    public CommentSearchRequest(String content, String nickname) {
        this.content = content;
        this.nickname = nickname;
    }

    public static CommentSearchRequest commentSearchRequest(String content, String nickname) {
        return CommentSearchRequest.builder()
                .content(content)
                .nickname(nickname).
                build();
    }

}
