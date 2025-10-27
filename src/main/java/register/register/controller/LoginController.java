package register.register.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import register.register.common.auth.JWTTokenProvider;
import register.register.domain.Student;
import register.register.web.dto.LoginDto;
import register.register.service.LoginService;
import register.register.common.validation.LoginValidator;


@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final LoginValidator loginValidator;
    private final JWTTokenProvider jwtTokenProvider;

    @Value("${jwt.expiration}")
    private int jwtExpireMin;

    @GetMapping
    public String LoginDto(@ModelAttribute("loginDto") LoginDto LoginDto) {

        // TODO 만약 토큰이 있다면, 로그인 페이지 대신 곧바로 수강과목 화면으로 보내기

        return "login/login-form";
    }

    @PostMapping
    public String login(@Validated @ModelAttribute("loginDto") LoginDto loginDto,
                        BindingResult bindingResult,
                        HttpServletResponse response) {

        // 필드 검증 로직 시작
        if (bindingResult.hasErrors()) {
            log.warn("error = {}", bindingResult);
            return "login/login-form";
        }

        // 오브젝트 에러는 걍 자바 코드로 쓰자
        Student student = loginValidator.validateStudent(loginDto, bindingResult);
        if (student == null) {
            return "login/login-form";
        }

        // 로그인 성공 처리
        log.info("{} 학생 로그인 성공", student.getStudentNumber());
        String jwtToken = jwtTokenProvider.createToken(student.getStudentNumber(), student.getRole());

        // Cookie로 토큰 반환
        Cookie tokenCookie = new Cookie("AUTH_TOKEN", jwtToken);
        tokenCookie.setHttpOnly(true); // XSS 방지
        tokenCookie.setPath("/"); // 사이트 전체 쿠키 유효
        tokenCookie.setMaxAge(jwtExpireMin * 60); // todo 세션 쿠키로 변경 고려해봅시다
        // tokenCookie.setSecure(true); // 배포하게 된다면 주석 제거
        response.addCookie(tokenCookie);

        return "redirect:/courses"; // 쿠키 포함 리다이렉션
    }


}
