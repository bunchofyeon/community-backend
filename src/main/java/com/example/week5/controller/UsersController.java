package com.example.week5.controller;

import com.example.week5.common.response.ApiResponse;
import com.example.week5.dto.request.auth.LoginRequest;
import com.example.week5.dto.request.auth.RegisterRequest;
import com.example.week5.dto.request.users.UserUpdateRequest;
import com.example.week5.dto.response.auth.LoginResponse;
import com.example.week5.dto.response.auth.RegisterResponse;
import com.example.week5.dto.response.users.UserResponse;
import com.example.week5.entity.Users;
import com.example.week5.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    // 사용자 - 회원가입시 사용자 이메일 중복 확인
    @GetMapping("/checkEmail")
    public ResponseEntity<?> checkIdDuplicate(@RequestParam String email) {
        usersService.checkEmailDuplicate(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 사용자 - 회원가입시 사용자 닉네임 중복 확인
    @GetMapping("/checkNickname")
    public ResponseEntity<?> checkNicknameDuplicate(@RequestParam String nickname) {
        usersService.checkNicknameDuplicate(nickname);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 사용자 - 회원 가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse successBody = usersService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입 성공", successBody));
    }

    // 사용자 - 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse successBody = usersService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("로그인 성공", successBody));
    }

    // 사용자 - 로그인 시 비밀번호 일치확인
    @PostMapping("/checkPwd")
    public ResponseEntity<ApiResponse<UserResponse>> check(
            @AuthenticationPrincipal Users users,
            @RequestBody Map<String, String> request) {
        String password = request.get("password");
        UserResponse successBody = usersService.check(users, password);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("비밀번호 일치", successBody));
    }

    // 사용자 - 사용자 정보 수정
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @AuthenticationPrincipal Users users,
            @RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse successBody = usersService.update(users, userUpdateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("정보 수정 성공", successBody));
    }

}
