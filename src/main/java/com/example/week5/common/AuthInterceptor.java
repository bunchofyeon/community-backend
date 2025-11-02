package com.example.week5.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
   * 로그인 했는지 검사
   * 매 컨트롤러마다 중복으로 세션 체크할 필요 없이 한 곳에서!
   * 인가 전 단계
 */

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle( // 컨트롤러 실행 직전에 호출되는 메서드
            HttpServletRequest requset,
            HttpServletResponse response,
            Object handler) throws Exception {

        // 컨트롤러가 아닌 요청(정적 리소스 등)은 대상이 아님 (통과시키기)
        if (!(handler instanceof HandlerMethod)) return true;

        // 기존 세션 가져오기 (생성 X)
        HttpSession session = requset.getSession(false);
        Object login = (session == null) ? null : session.getAttribute("LOGIN_USER");
        if (login == null) { // 로그인 정보가 없으면
            // 컨트롤러 실행 직전이니까 GlobalExceptionHandler 못씀
            // 그래서 예외처리 이렇게!!
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\":false,\"message\":\"로그인이 필요합니다.\"}");
            return false;
        }
        return true;
    }
}
