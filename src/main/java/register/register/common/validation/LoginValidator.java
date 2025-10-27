package register.register.common.validation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import register.register.domain.Student;
import register.register.common.exception.LoginAuthenticationException;
import register.register.controller.dto.LoginDto;
import register.register.service.LoginService;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginValidator implements Validator {

    private final LoginService loginService;


    @Override
    public boolean supports(Class<?> clazz) {
        return LoginDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginDto form = (LoginDto) target;

        if (!StringUtils.hasText(form.getStudentNumber())) {
            errors.rejectValue("studentNumber","required");
        }

        if (!StringUtils.hasText(form.getPassword())) {
            errors.rejectValue("password","required");
        }

    }

    public Student validateStudent(@Valid LoginDto loginDto, BindingResult bindingResult) {

        Student loginStudent;

        try {
            loginStudent = loginService.login(loginDto); // 이 메서드가 학번 조회 + PW 검증 모두 처리
        } catch (LoginAuthenticationException e) {
            bindingResult.reject("wrongStudentNumberOrPassword", e.getMessage());
            log.warn("올바르지 않은 아이디(학번) 혹은 비밀 번호 입력, errors = {}", bindingResult);
            return null;
        }

        return loginStudent;
    }
}
