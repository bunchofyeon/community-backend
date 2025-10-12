package com.example.week5.service;

import com.example.week5.common.exception.custom.ResourceNotFoundException;
import com.example.week5.dto.request.posts.PostWriteRequest;
import com.example.week5.dto.response.posts.PostDetailsResponse;
import com.example.week5.dto.response.posts.PostWriteResponse;
import com.example.week5.entity.Posts;
import com.example.week5.entity.Users;
import com.example.week5.repository.PostsRepository;
import com.example.week5.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PostsService {

    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    // 게시글 등록
    public PostWriteResponse write(PostWriteRequest postWriteRequest, Users users) {

        // 1) 작성자 조회
        Users writer = usersRepository.findByEmail(users.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Users"));

        // 2) DTO -> Entity
        Posts posts = PostWriteRequest.ofEntity(postWriteRequest);

        // 3) 작성자 매핑
        posts.setMappingUsers(writer);

        // 4) 저장
        Posts savedPosts = postsRepository.save(posts);

        // 5) 응답 변환
        return PostWriteResponse.fromEntity(savedPosts);
    }

    // 게시글 상세 조회
    public PostDetailsResponse detail(Long postId) {
        Posts findPost = postsRepository.findByIdWithUsers(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post"));

        // 조회수 증가
        // findBoard.upViewCount();

        return PostDetailsResponse.fromEntity(findPost);
    }

    // 게시글 삭제
    public void delete(Long postId) {
        postsRepository.deleteById(postId);
    }
}
