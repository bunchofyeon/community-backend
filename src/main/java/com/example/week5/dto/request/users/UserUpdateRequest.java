package com.example.week5.dto.request.users;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {

    private String email;
    private String password;
    private String passwordCheck;
    private String nickname;
    private String profileImageUrl;

    @Builder
    public UserUpdateRequest(String email, String password, String passwordCheck, String nickname, String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
