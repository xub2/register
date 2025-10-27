package register.register.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import register.register.common.interceptor.RegisterTimeInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RegisterTimeInterceptor registerTimeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(registerTimeInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",          // 홈
                        "/login",     // 로그인 페이지
                        "/logout",    // 로그아웃 처리
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/favicon.ico",
                        "/application-unavailable",
                        "/error"
                );
    }
}
