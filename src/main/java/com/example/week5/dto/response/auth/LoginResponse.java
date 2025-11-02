package com.example.week5.dto.response.auth;

import com.example.week5.entity.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {

    private String email;

    @Builder
    public LoginResponse(String email) {
        this.email = email;
    }

    // Entity -> DTO
    public static LoginResponse fromEntity(Users users) {
        return LoginResponse.builder()
                .email(users.getEmail())
                .build();
    }

}
