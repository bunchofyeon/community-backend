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
import com.example.week5.security.jwt.JwtTokenUtil;
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

    private final JwtTokenUtil jwtTokenUtil;

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
        if (!password.equals(passwordCheck)) {
            throw new BadRequestException("패스워드 불일치");
        }
    }

    // 2. 로그인 구현
    public LoginResponse login(LoginRequest loginRequest) {
        // 1) 이메일로 사용자 조회
        Users user = usersRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("이메일과 비밀번호를 확인해주세요."));

        // 2) 비밀번호 입력
        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("이메일과 비밀번호를 확인해주세요.");
        }
        // 3) 통과 -> 토큰 생성
        String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole() != null ? user.getRole().name() : null);

        // 4) 로그인 완료
        return LoginResponse.fromEntity(user, token);
    }

    // 비말번호 인코딩 체크 (사용자가 입력한 비밀번호와 디비에 저장된 비밀번호가 같은지 체크)
    private void checkEncodePassword(String rawPassword, String encodedPassword) {
        if (!encoder.matches(rawPassword, encodedPassword)) {
            throw new BadRequestException("패스워드 불일치");
        }
    }

    // 3. 마이페이지
    public UserResponse check(String email, String password) {
        Users users = usersRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Users"));
        checkEncodePassword(password, users.getPassword());
        return UserResponse.fromEntity(users);
    }

    // 3-2. 사용자 정보 수정
    public UserResponse update(Users users, UserUpdateRequest userUpdateRequest) {
        checkPassword(userUpdateRequest.getPassword(), userUpdateRequest.getPasswordCheck());
        String encodePwd = encoder.encode(userUpdateRequest.getPassword());
        Users updateUsers = usersRepository.findByEmail(users.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Member")
        );

        // encodePwd : 디비에는 암호화된 비밀번호를 저장하니까 수정할때도 인코딩해서 넣겠다는 뜻
        updateUsers.update(encodePwd, userUpdateRequest.getNickname(), userUpdateRequest.getProfileImageUrl());
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


}
