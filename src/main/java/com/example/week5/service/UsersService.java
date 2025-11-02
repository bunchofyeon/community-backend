package com.example.week5.service;

import com.example.week5.common.exception.custom.BadRequestException;
import com.example.week5.common.exception.custom.ConflictedException;
import com.example.week5.common.exception.custom.ResourceNotFoundException;
import com.example.week5.dto.request.auth.LoginRequest;
import com.example.week5.dto.request.auth.RegisterRequest;
import com.example.week5.dto.request.users.UserUpdateRequest;
import com.example.week5.dto.response.auth.LoginResponse;
import com.example.week5.dto.response.auth.RegisterResponse;
import com.example.week5.dto.response.users.UserResponse;
import com.example.week5.entity.Users;
import com.example.week5.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UsersService {

    private final PasswordEncoder encoder;
    private final UsersRepository usersRepository;

    // 1. 회원가입
    // 1-1. 이메일 중복 확인
    public HttpStatus checkEmailDuplicate(String email) {
        isExistUserEmail(email);
        return HttpStatus.OK;
    }

    // 이메일 중복 체크
    private void isExistUserEmail(String email) {
        if (usersRepository.findByEmail(email).isPresent()) {
            throw new ConflictedException("이미 사용 중인 이메일입니다.");
        }
    }

    // 1-2. 닉네임 중복 확인
    public HttpStatus checkNicknameDuplicate(String nickname) {
        isExistUserNickname(nickname);
        return HttpStatus.OK;
    }

    // 닉네임 중복 체크
    private void isExistUserNickname(String nickname) {
        if (usersRepository.findByNickname(nickname).isPresent()) {
            throw new ConflictedException("이미 사용 중인 닉네임입니다.");
        }
    }

    // 1-3. 회원가입 구현
    public RegisterResponse register(RegisterRequest registerRequest) {
        // 1) 이메일 중복 확인
        isExistUserEmail(registerRequest.getEmail());

        // 2) 닉네임 중복 확인
        isExistUserNickname(registerRequest.getNickname());

        // 3) 패스워드 일치하는지 체크
        checkPassword(registerRequest.getPassword(), registerRequest.getPasswordCheck());

        // 4) 패스워드 암호화
        String encodePassword = encoder.encode(registerRequest.getPassword());
        registerRequest.setPassword(encodePassword);

        // 5) 저장
        Users saveUser = usersRepository.save(
                RegisterRequest.ofEntity(registerRequest));
        return RegisterResponse.fromEntity(saveUser);

    }

    // 비밀번호 일치 검사
    private void checkPassword(String password, String passwordCheck) {
        if (password == null || passwordCheck == null) {
            throw new BadRequestException("비밀번호를 입력 안함");
        }
        if (!password.equals(passwordCheck)) {
            throw new BadRequestException("패스워드 불일치");
        }
    }

    // 2. 로그인 구현
    // LoginRequest 가 아니라 LoginResponse (인증 성공 후 발급한 JWT 토큰을 주는거니까...)
    // 2. 로그인 구현
    public LoginResponse login(LoginRequest loginRequest) {
        // 1) 자격 증명 검증
        Users user = usersRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new BadRequestException("이메일과 비밀번호를 확인해주세요.")
        );

        // 2) 응답은 Users 엔티티 기준 (이메일로 조회)
        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("이메일과 비밀번호를 확인해주세요.");
        }

        return LoginResponse.fromEntity(user);
    }

    // 비말번호 인코딩 체크 (사용자가 입력한 비밀번호와 디비에 저장된 비밀번호가 같은지 체크)
    private void checkEncodePassword(String rawPassword, String encodedPassword) {
        if (!encoder.matches(rawPassword, encodedPassword)) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        }
    }
/*
    // 3. 마이페이지
    // 3-1. 사용자 정보 조회 (비밀번호 검증 후 정보 리턴)
    public UserResponse check(Users users, String password) {
        checkEncodePassword(password, users.getPassword());
        return UserResponse.fromEntity(users);
    }

    // 3-2. 사용자 정보 수정
    public UserResponse update(Users users, UserUpdateRequest userUpdateRequest) {

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(userUpdateRequest.getCurrentPassword(), users.getPassword())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 바꿀 사용자 찾기
        Users updateUsers = usersRepository.findByEmail(users.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("사용자를 찾을 수 없습니다.")
        );

        // 닉네임 바꾸기
        if (!users.getNickname().equals(userUpdateRequest.getNickname())) {
            updateUsers.changeNickname(userUpdateRequest.getNickname());
        }

        // 프로필 사진 바꾸기
        // ...

        // 3) 새 비밀번호 둘 다 있을 때만 변경
        if (userUpdateRequest.getPassword() != null || userUpdateRequest.getPasswordCheck() != null) {
            checkPassword(userUpdateRequest.getPassword(), userUpdateRequest.getPasswordCheck());
            String encodePwd = encoder.encode(userUpdateRequest.getPassword());
            updateUsers.changePassword(encodePwd);
        }

        return UserResponse.fromEntity(updateUsers);
    }

    // 4. 마이페이지 - 관리자, 회원 탈퇴
    // soft delete
    public void delete(Long id) {
        usersRepository.deleteById(id);
    }

    // 5. 관리자 - 회원 전체 조회
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<Users> users = usersRepository.findAll(pageable);
        List<UserResponse> list = users.getContent().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, users.getTotalElements());
    }

*/
}