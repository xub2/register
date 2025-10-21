package register.register.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import register.register.exception.LoginAuthenticationException;
import register.register.service.login.LoginForm;
import register.register.domain.Student;
import register.register.service.login.LoginService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @GetMapping
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login/login-form";
    }

    @PostMapping
    public String login(@Valid @ModelAttribute LoginForm form,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectUrl,
                        HttpServletRequest request) {

        if (!StringUtils.hasText(form.getStudentNumber())) {
            bindingResult.rejectValue("studentNumber","required");
        }

        if (!StringUtils.hasText(form.getPassword())) {
            bindingResult.rejectValue("password","required");
        }
        if (bindingResult.hasErrors()) {
            log.warn("error = {}", bindingResult);
            return "login/login-form";
        }


        // 만약 ID / PW 가 맞지 않으면 글로벌 오류 터트리기
        Student loginStudent;

        try {
            loginStudent = loginService.login(form.getStudentNumber(), form.getPassword());
        } catch (LoginAuthenticationException e) {
            log.warn("올바르지 않은 아이디(학번) 혹은 비밀 번호 입력");
//            bindingResult.addError(new ObjectError("form", "올바른 아이디(학번) 혹은 비밀번호가 아닙니다."));
            bindingResult.reject("wrongStudentNumberOrPassword");
            return "login/login-form";
        }


        //todo 여기부턴 로그인 성공 처리 -> 세션 필요함
        log.info("로그인 성공");
        return "redirect:/courses";
    }
}
