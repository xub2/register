package register.register.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class RegisterTimeInterceptor implements HandlerInterceptor {

    @Value("${registration.startTime}")
    private LocalDateTime startTime;

    @Value("${registration.endTime}")
    private LocalDateTime endTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        LocalDateTime now = LocalDateTime.now();
        String message;

        if (now.isBefore(startTime)) {
            // 2. [수정] 시작 전 메시지 설정
            message = "수강 신청 기간이 아닙니다. (시작 전)";
            // 3. [수정] URL 인코딩 및 리다이렉트
            String encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            response.sendRedirect("/application-unavailable?msg=" + encodedMsg);
            return false;
        }

        if (now.isAfter(endTime)) {
            // 2. [수정] 마감 메시지 설정
            message = "수강 신청 기간이 마감되었습니다.";
            // 3. [수정] URL 인코딩 및 리다이렉트
            String encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            response.sendRedirect("/application-unavailable?msg=" + encodedMsg);
            return false;
        }

        return true;
    }
}
