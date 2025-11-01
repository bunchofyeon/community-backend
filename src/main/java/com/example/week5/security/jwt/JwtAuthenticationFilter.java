package com.example.week5.security.jwt;

import com.example.week5.common.exception.custom.UnauthorizedException;
import com.example.week5.entity.Users;
import com.example.week5.repository.UsersRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UsersRepository usersRepository;

    // 화이트리스트: 필터 제외 경로
    private static final String[] EXCLUDED_PATHS = {
            "/users/login", "/users/register", "/refresh", "/error", "/docs"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true; // CORS preflight
        if (p.startsWith("/assets") || p.startsWith("/static") || "/favicon.ico".equals(p)) return true;
        return Arrays.stream(EXCLUDED_PATHS).anyMatch(p::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // 1) 토큰 추출 (헤더 우선, 없으면 쿠키)
        Optional<String> tokenOpt = extractTokenFromHeader(request)
                .or(() -> extractTokenFromCookie(request));

        if (tokenOpt.isEmpty()) {
            unauthorized(response, "Missing token");
            return;
        }

        String token = tokenOpt.get();

        try {
            // 2) 토큰 유효성 검증
            if (!jwtTokenUtil.validateToken(token)) {
                unauthorized(response, "Invalid or expired token");
                return;
            }
            // 3) 클레임 추출
            Claims c = jwtTokenUtil.parseClaims(token);
            String email = c.get("email", String.class);
            String role  = c.get("role", String.class);

            // 컨트롤러에서 꺼내 쓰도록 인증 결과 주입
            request.setAttribute("email", email);
            request.setAttribute("role", role);

            // 요청마다 상태 확인
            Users user = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new UnauthorizedException("권한이 없습니다."));

            chain.doFilter(request, response);

        } catch (UnauthorizedException e) {
            unauthorized(response, e.getMessage());
        } catch (Exception e) {
            log.error("JWT filter error", e);
            unauthorized(response, "Authentication failed");
        }
    }

    private Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        String h = request.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) return Optional.of(h.substring(7));
        return Optional.empty();
    }

    private Optional<String> extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();
        return Arrays.stream(cookies)
                .filter(c -> "accessToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private void unauthorized(HttpServletResponse res, String msg) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"success\":false,\"message\":\"" + msg + "\"}");
    }

    private Long parseLong(String v) {
        try { return Long.parseLong(v); }
        catch (Exception e) { throw new UnauthorizedException("Invalid subject claim"); }
    }
}