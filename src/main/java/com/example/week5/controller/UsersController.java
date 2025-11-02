package com.example.week5.controller;

import com.example.week5.common.response.ApiResponse;
import com.example.week5.dto.request.auth.LoginRequest;
import com.example.week5.dto.request.auth.RegisterRequest;
import com.example.week5.dto.response.auth.LoginResponse;
import com.example.week5.dto.response.auth.RegisterResponse;
import com.example.week5.service.CommentsService;
import com.example.week5.service.PostsService;
import com.example.week5.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final PostsService postsService;
    private final CommentsService commentsService;

    // 회원가입시 이메일 중복 확인
    @GetMapping("/checkEmail")
    public ResponseEntity<?> checkIdDuplicate(@RequestParam String email) {
        usersService.checkEmailDuplicate(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 회원가입시 닉네임 중복 확인
    @GetMapping("/checkNickname")
    public ResponseEntity<?> checkNicknameDuplicate(@RequestParam String nickname) {
        usersService.checkNicknameDuplicate(nickname);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse successBody = usersService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입 성공", successBody));
    }

    // 로그인 - 세션
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        LoginResponse successBody = usersService.login(loginRequest);
        HttpSession session = request.getSession(true);// 서버가 세션ID 생성
        session.setAttribute("sessionID", successBody); // 브라우저한테 세션ID 전달
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("로그인 성공", successBody));
    }

    /*
    // 사용자 정보 수정
    @PatchMapping ("/update")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @Valid @RequestBody UserUpdateRequest userUpdateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Users users = customUserDetails.getUsers();
        UserResponse successBody = usersService.update(users, userUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("정보 수정 성공", successBody));
    }

    // 마이페이지 - 사용자별 게시글 조회
    @GetMapping("/myPosts")
    public ResponseEntity<ApiResponse<Page<PostListResponse>>> myPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Users users = customUserDetails.getUsers();
        Page<PostListResponse> listDTO = postsService.getMyPosts(pageable, users);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("사용자 게시글 목록 조회", listDTO));
    }

    // 마이페이지 - 사용자별 댓글 조회
    @GetMapping("/myComments")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> myComments(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Users users = customUserDetails.getUsers();
        Page<CommentResponse> listDTO = commentsService.getMyComments(pageable, users);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("사용자 댓글 목록 조회", listDTO));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Users me = customUserDetails.getUsers();
        return ResponseEntity.ok(
                ApiResponse.success("me", UserResponse.fromEntity(me))
        );
    }

    // 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMe(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        usersService.delete(customUserDetails.getUsers().getId());
        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴", null));
    }

    // 관리자가 유저 탈퇴 시킬때 (soft delete 해야되나?)
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long userId) {
        usersService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("회원 탈퇴", null));
    }

     */

}