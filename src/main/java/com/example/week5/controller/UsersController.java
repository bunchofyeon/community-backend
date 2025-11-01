package com.example.week5.controller;

import com.example.week5.common.response.ApiResponse;
import com.example.week5.dto.request.auth.LoginRequest;
import com.example.week5.dto.request.auth.RegisterRequest;
import com.example.week5.dto.response.auth.LoginResponse;
import com.example.week5.dto.response.auth.RegisterResponse;
import com.example.week5.dto.response.users.UserResponse;
import com.example.week5.service.CommentsService;
import com.example.week5.service.PostsService;
import com.example.week5.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse successBody = usersService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입 성공", successBody));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse successBody = usersService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("로그인 성공", successBody));
    }

    // 비밀번호 일치확인
    @PostMapping("/checkPwd")
    public ResponseEntity<ApiResponse<UserResponse>> check(
            HttpServletRequest request,
            @RequestBody Map<String, String> requestBody) {
        String email = (String) request.getAttribute("email");
        String password = requestBody.get("password");
        UserResponse successBody = usersService.check(email, password);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("비밀번호 일치", successBody));
    }

    /*
    // 사용자 정보 수정
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody UserUpdateRequest userUpdateRequest) {
        Users users = customUserDetails.getUsers();
        UserResponse successBody = usersService.update(users, userUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("정보 수정 성공", successBody));
    }

    // 마이페이지 - 사용자별 게시글 조회
    // 여기에 구현하는게 맞을까 ㅠㅠ
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

    // 마이페이지 - 회원, 관리자 탈퇴 (soft delete)
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long userId) {
        usersService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("회원 탈퇴", null));
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

     */
}
