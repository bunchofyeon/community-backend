package com.example.week5.dto.request.comments;

import com.example.week5.entity.Comments;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.stream.events.Comment;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {

    private String content;

    @Builder
    public CommentRequest(String content) {
        this.content = content;
    }

    public static Comments ofEntity(CommentRequest dto) {
        return Comments.builder()
                .content(dto.getContent())
                .build();
    }
}
