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
import org.springframework.util.StringUtils; // 문자열 유틸리티 (null 또는 빈 문자열 체크)

// Java 암호화 API (비밀 키 생성용)
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
public class JWTTokenFilter extends GenericFilter { // GenricFilter는 모든 요청을 가로챈다


    // JWT 서명/검증에 사용할 비밀 키 (HS512 알고리즘)
    private final SecretKey secretKey;
    // 사용할 쿠키의 이름 (LoginController와 동일)
    private final String AUTH_COOKIE_NAME = "AUTH_TOKEN";

    public JWTTokenFilter(@Value("${jwt.secret}") String secretKeyBase64) {
        // application.properties의 jwt.secret 값을 Base64 디코딩하여 byte 배열로 변환
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyBase64);

        // 디코딩된 byte 배열과 HS512 알고리즘을 사용하여 SecretKey 객체 생성
        // (JWTTokenProvider가 토큰 생성 시 사용한 키 생성 방식과 완벽히 동일해야 함)
        this.secretKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    // 모든 HTTP 요청이 들어올 때마다 이 메서드가 실행됨
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // ServletRequest/Response를 HTTP용으로 형변환 (URL 경로, 쿠키 등을 다루기 위함)
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 현재 요청된 URL 경로 추출 (ex : "/", "/login", "/courses")
        String path = httpRequest.getRequestURI();

        // 이 요청이 인증 없이 접근 가능한지 여부를 판단할 변수
        boolean isPublicPath = false;

        // "/css/", "/js/" 등 특정 폴더 하위 경로는 모두 공개 처리 (startsWith: ~로 시작하는지)
        List<String> startsWithPaths = List.of("/css/", "/js/", "/images/", "/error"); // "/" 추가됨 (이전 코드 버그 수정 반영)
        if (startsWithPaths.stream().anyMatch(path::startsWith)) {
            isPublicPath = true;
        }

        // "/", "/login", "/logout" 등 정확히 일치해야 하는 경로는 공개 처리 (equals: 정확히 같은지)
        List<String> exactMatchPaths = List.of("/", "/login", "/logout", "/favicon.ico", "/error"); // "/" 중복이지만 괜찮음
        if (!isPublicPath && exactMatchPaths.stream().anyMatch(path::equals)) {
            isPublicPath = true;
        }

        // 만약 공개 경로라면 (isPublicPath == true)
        if (isPublicPath) {
            // 토큰 검증 로직을 모두 건너뛰고, 다음 필터(또는 Controller)로 요청을 바로 전달
            log.debug("공개경로 [{}] 로 요청. JWT 필터 적용 X.", path);
            chain.doFilter(request, response);

            return;
        }


        // 비공개 경로 처리 로직 시작
        // 쿠키 목록에서 "AUTH_TOKEN" 쿠키를 찾아 토큰 문자열 추출
        Optional<String> token = extractTokenFromCookie(httpRequest);

        // 만약 토큰이 쿠키에 없다면
        if (token.isEmpty()) {
            // 401 Unauthorized 에러 응답 전송 (로그인되지 않은 사용자)
            log.warn("비공개 경로 [{}] 로 토큰 없이 접속 시도 : 접근 거절", path);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "쿠키에 토큰 없음");
            // 필터 로직 종료하고 Controller로 요청 전달 안 함
            return;
        }

        // 쿠키에서 추출한 실제 JWT 토큰 문자열 (예: "eyJhb...")
        String jwtToken = token.get();

        // 토큰 검증 시작
        try {
            // jjwt 라이브러리를 사용하여 토큰 파싱 및 검증
            Claims claims = Jwts.parserBuilder()
                    // 검증에 사용할 비밀 키 설정 (Provider가 서명 시 사용한 키와 동일해야 함)
                    .setSigningKey(secretKey)
                    .build()
                    // 토큰 문자열을 파싱하고 서명을 검증 (여기서 실패하면 JwtException 발생)
                    .parseClaimsJws(jwtToken)
                    // 검증 성공 시, 토큰의 Payload(Claims) 부분 추출
                    .getBody();

            // Spring Security가 사용할 권한 목록 생성
            List<GrantedAuthority> authorities = new ArrayList<>();

            // 토큰의 Payload에서 role 값을 읽어옴
            String role = claims.get("role", String.class);
            // 30. Role 값이 토큰에 있다면 -> 정상
            if (role != null) {
                // "ROLE_" 를 붙여 SimpleGrantedAuthority 객체 생성 (Spring Security 표준)
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            } else {
                // (방어 로직) 혹시 Role 값이 없다면, 기본 권한 "ROLE_USER" 부여
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                log.warn("role claim missing in token for subject: {}", claims.getSubject());
            }

            // Spring Security의 UserDetails 객체 생성
            //     - 첫번째 인자: 사용자 식별자 (토큰의 Subject = 학번)
            //     - 두번째 인자: 비밀번호 (JWT 방식에서는 불필요하므로 빈 문자열)
            //     - 세번째 인자: 위에서 만든 권한 목록
            UserDetails userDetails = new User(claims.getSubject(), "", authorities);

            // Spring Security의 Authentication 객체 생성 (UsernamePasswordAuthenticationToken 사용)
            // 이 객체가 "현재 인증된 사용자"를 나타냄 -> 이후부턴 courses 컨트롤러 접근 가능
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, jwtToken, userDetails.getAuthorities());

            // [핵심] SecurityContextHolder에 위에서 만든 Authentication 객체를 저장
            // 이렇게 하면, 현재 요청을 처리하는 동안 Spring Security는 이 사용자를 "인증된 사용자"로 인식함
            // Controller의 @AuthenticationPrincipal 등이 이 정보를 사용함 -> 이후 코드들에서 해당 사용자 정보 꺼내오기 가능
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("학생 [{}] 이 인증되었습니다. 권한 : {} / 요청경로 [{}].", claims.getSubject(), authorities, path);
            // 인증 성공 - 다음 필터(또는 Controller)로 요청 전달
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) { // 토큰 만료 시
            log.warn("토큰이 만료되었습니다. 요청 경로: {}", path);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired JWT token");
        } catch (JwtException | IllegalArgumentException e) { // 토큰 서명 불일치, 형식 오류 등
            log.warn("올바르지 않은 토큰입니다. 요청 경로: {}", path, e); // 로그에 예외 정보 포함
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
        }finally {
            // 토큰 검증용 필터 종료 눈으로 확인하기 위해 찍는 로그
            log.debug("JWT 토큰 검증 Filter 종료");
        }
    }


    // HttpServletRequest 객체에서 쿠키 배열을 받아 "AUTH_TOKEN" 쿠키 값 찾기
    private Optional<String> extractTokenFromCookie(HttpServletRequest request) {
        // 쿠키가 아예 없으면 빈 Optional 반환
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        for (Cookie cookie : request.getCookies()) {
            // 쿠키 이름이 "AUTH_TOKEN"과 일치하는지 확인
            if (cookie.getName().equals(AUTH_COOKIE_NAME)) {
                // 쿠키 값(토큰 문자열) 추출
                String token = cookie.getValue();
                // 토큰 값이 null이 아니고 비어있지 않은지 확인 (StringUtils 사용)
                if (StringUtils.hasText(token)) {
                    // 유효한 토큰 문자열을 Optional에 담아 반환
                    return Optional.of(token);
                }
            }
        }
        // 모든 쿠키를 확인했지만 "AUTH_TOKEN"을 찾지 못했거나 값이 비어있으면 빈 Optional 반환
        return Optional.empty();
    }
}