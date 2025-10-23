package register.register.common.auth;

import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie; 
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j; 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils; 

import javax.crypto.SecretKey; 
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64; 
import java.util.List;
import java.util.Optional;

@Slf4j 
@Component
public class JWTTokenFilter extends GenericFilter {

    private final SecretKey secretKey;
    private final String AUTH_COOKIE_NAME = "AUTH_TOKEN";

    public JWTTokenFilter(@Value("${jwt.secret}") String secretKeyBase64) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyBase64);

        // Provider와 동일한 HS512 방식 사용
        this.secretKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        boolean isPublicPath = false;

        List<String> startsWithPaths = List.of("/css/", "/js/", "/images/");
        if (startsWithPaths.stream().anyMatch(path::startsWith)) {
            isPublicPath = true;
        }


        List<String> exactMatchPaths = List.of("/", "/login", "/logout","/favicon.ico");
        if (!isPublicPath && exactMatchPaths.stream().anyMatch(path::equals)) {
            isPublicPath = true;
        }

        if (isPublicPath) {
            log.debug("Public path [{}] requested. Skipping JWT filter.", path);
            chain.doFilter(request, response);
            return;
        }

        Optional<String> token = extractTokenFromCookie(httpRequest);

        if (token.isEmpty()) {
            log.warn("Non-public path [{}] requested without token. Rejecting.", path);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token not found in cookie");
            return;
        }

        String jwtToken = token.get();

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey) // 이제 이 key는 HS512로 생성됨
                    .build()
                    .parseClaimsJws(jwtToken) // 쿠키에서 꺼낸 토큰
                    .getBody(); // 거기서 studentName 파싱

            List<GrantedAuthority> authorities = new ArrayList<>();

//            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            String role = claims.get("role", String.class);
            if (role != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                log.warn("role 없이 인가된 토큰 : {}", claims.getSubject());
            }

            UserDetails userDetails = new User(claims.getSubject(), "", authorities);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, jwtToken, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("User [{}] authenticated successfully for path [{}].", claims.getSubject(), path);
            chain.doFilter(request, response); // [필수] 인증 성공 시 다음 필터로

        } catch (ExpiredJwtException e) {
            log.warn("Request with expired JWT token rejected. Path: {}", path);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired JWT token");
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Request with invalid JWT token rejected. Path: {}", path);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
        }
    }

    /**
     * HttpOnly 쿠키에서 토큰을 추출하는 헬퍼 메서드
     */
    private Optional<String> extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(AUTH_COOKIE_NAME)) {
                String token = cookie.getValue();
                if (StringUtils.hasText(token)) {
                    return Optional.of(token);
                }
            }
        }
        return Optional.empty();
    }
}
