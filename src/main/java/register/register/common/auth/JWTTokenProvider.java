package register.register.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import register.register.domain.Role;


import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JWTTokenProvider {

    private final String secretKey;

    private final int expiration;

    private Key SECRET_KEY;

    public JWTTokenProvider(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") int expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
        this.SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey),
                SignatureAlgorithm.HS512.getJcaName()); // 알고리즘 선택 -> 나중에 이부분이 JWT 토큰의 맨 뒷부분 SIGNATURE 부분이 된다
    }

    public String createToken(String studentNumber, Role role) {
        // claims 는 jwt토큰의 payload 부분 의미
        Claims claims = Jwts.claims().setSubject(studentNumber);

        // [수정] claims에 role 추가 (Enum의 이름, 예: "STUDENT" 또는 "ADMIN")
        claims.put("role", role.name());

        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration * 60 * 1000L))
                .signWith(SECRET_KEY)
                .compact();

        return token;
    }


}
