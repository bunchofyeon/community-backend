package com.example.week5.controller;

import com.example.week5.common.response.ApiResponse;
import com.example.week5.dto.request.posts.PostUpdateRequest;
import com.example.week5.dto.request.posts.PostWriteRequest;
import com.example.week5.dto.response.posts.PostDetailsResponse;
import com.example.week5.dto.response.posts.PostListResponse;
import com.example.week5.dto.response.posts.PostWriteResponse;
import com.example.week5.entity.Users;
import com.example.week5.security.jwt.CustomUserDetails;
import com.example.week5.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostsController {

    private final PostsService postsService;

    // 게시글 목록 조회
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Page<PostListResponse>>> boardList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostListResponse> listDTO = postsService.getAllPosts(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("게시글 목록 조회", listDTO));
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailsResponse>> detail(@PathVariable("postId") Long postId) {
        PostDetailsResponse findPostDTO = postsService.detail(postId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("게시글 조회", findPostDTO));
    }

    // 게시글 작성
    @PostMapping("/write")
    public ResponseEntity<ApiResponse<PostWriteResponse>> write(
            @RequestBody PostWriteRequest postWriteRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Users users = customUserDetails.getUsers();

        PostWriteResponse savePostDTO = postsService.write(postWriteRequest, users);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("게시글 작성", savePostDTO));
    }

    // 게시글 수정 (게시글 상세 조회 -> 수정)
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailsResponse>> update(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest postUpdateRequest) {
        PostDetailsResponse updatePostDTO = postsService.update(postId, postUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("게시글 수정", updatePostDTO));
    }

    // 게시글 삭제 (soft delete)
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long postId) {
        postsService.delete(postId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("게시글 삭제", null));
    }

}
