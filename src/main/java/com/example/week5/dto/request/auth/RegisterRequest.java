package com.example.week5.dto.request.auth;

import com.example.week5.entity.Role;
import com.example.week5.entity.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    private String email;
    private String password;
    private String passwordCheck;
    private String nickname;
    private String profileImageUrl;

    @Builder
    public RegisterRequest(String email, String password, String passwordCheck, String nickname, String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    // DTO -> Entity
    public static Users ofEntity(RegisterRequest dto) {
        return Users.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .profileImageUrl(dto.getProfileImageUrl())
                .role(Role.USER)
                .build();
    }

}
