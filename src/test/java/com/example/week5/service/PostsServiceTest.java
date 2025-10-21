package com.example.week5.service;

import com.example.week5.common.exception.custom.ResourceNotFoundException;
import com.example.week5.dto.request.posts.PostWriteRequest;
import com.example.week5.dto.response.posts.PostWriteResponse;
import com.example.week5.entity.Posts;
import com.example.week5.entity.Users;
import com.example.week5.repository.PostsRepository;
import com.example.week5.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostsServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PostsRepository postsRepository;

    @InjectMocks
    private PostsService postsService;
    // PostsServiceTest가 아니라...

    @Test
    @DisplayName("wirte(), 게시글 작성 성공")
    void writeSuccess() {
        //given
        Long userId = 1L;
        String title = "제목";
        String content = "내용";

        Users users = Users.builder()
                .email("test@test")
                .nickname("태스트")
                .build();

        PostWriteRequest postWriteRequest = new PostWriteRequest("제목", "내용");

        Posts saved = Posts.builder()
                .title(title)
                .content(content)
                .users(users)
                .build();

        given(usersRepository.findByEmail(users.getEmail())).willReturn(Optional.of(users));
        given(postsRepository.save(any(Posts.class))).willReturn(saved);

        // when
        PostWriteResponse response = postsService.write(postWriteRequest, users);

        // then
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContent()).isEqualTo(content);
        verify(usersRepository).findByEmail(users.getEmail());
        verify(postsRepository).save(any(Posts.class));
    }

    @Test
    @DisplayName("사용자 조회 실패 -> 게시글 작성 실패")
    void writeFail() {

        //given
        Users users = Users.builder()
                .email("none@none")
                .nickname("도둑")
                .build();

        PostWriteRequest postWriteRequest = new PostWriteRequest("제목", "내용");

        given(usersRepository.findByEmail(users.getEmail()))
                .willReturn(Optional.empty()); // 사용자 비어있음

        // when
        // then
        assertThatThrownBy(() -> postsService.write(postWriteRequest, users))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Users");

        verify(postsRepository, never()).save(any(Posts.class));

    }
}
