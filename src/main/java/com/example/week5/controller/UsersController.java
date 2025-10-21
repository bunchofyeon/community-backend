package com.example.week5.controller;

import com.example.week5.common.response.ApiResponse;
import com.example.week5.dto.request.auth.LoginRequest;
import com.example.week5.dto.request.auth.RegisterRequest;
import com.example.week5.dto.request.users.UserUpdateRequest;
import com.example.week5.dto.response.auth.LoginResponse;
import com.example.week5.dto.response.auth.RegisterResponse;
import com.example.week5.dto.response.comments.CommentResponse;
import com.example.week5.dto.response.posts.PostListResponse;
import com.example.week5.dto.response.users.UserResponse;
import com.example.week5.entity.Users;
import com.example.week5.security.jwt.CustomUserDetails;
import com.example.week5.service.CommentsService;
import com.example.week5.service.PostsService;
import com.example.week5.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.week5.entity.QUsers.users;

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

    // 로그인 시 비밀번호 일치확인
    // @AuthenticationPrincipal CustomUserDetails customUserDetails로 변경
    // -> Users users = customUserDetails.getUsers(); 추가해서 users 조회하도록
    @PostMapping("/checkPwd")
    public ResponseEntity<ApiResponse<UserResponse>> check(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody Map<String, String> request) {
        String password = request.get("password");
        Users users = customUserDetails.getUsers();
        UserResponse successBody = usersService.check(users, password);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("비밀번호 일치", successBody));
    }

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

    // 사용자 본인 탈퇴
}
