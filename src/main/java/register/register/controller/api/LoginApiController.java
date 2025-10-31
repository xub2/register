package register.register.controller.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import register.register.common.exception.LoginAuthenticationException;
import register.register.controller.dto.ErrorResponse;
import register.register.controller.dto.LoginDto;
import register.register.domain.Student;
import register.register.security.auth.JWTTokenProvider;
import register.register.service.LoginService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginApiController {

    private final LoginService loginService;
    private final JWTTokenProvider jwtTokenProvider;


    @PostMapping("/auth/login")
    public ResponseEntity<LoginStudentResponse> login(@RequestBody LoginStudentRequest loginStudentRequest) {

        LoginDto loginDto = new LoginDto(loginStudentRequest.getStudentNumber(), loginStudentRequest.getPassword());
        Student student = loginService.login(loginDto);

        log.info("{} 학번 학생 로그인 성공 ", loginStudentRequest.getStudentNumber());

        String token = jwtTokenProvider.createToken(student.getStudentNumber(), student.getRole());

        return ResponseEntity.ok(new LoginStudentResponse(token));
    }


    @Data
    @AllArgsConstructor
    static class LoginStudentRequest {
        private String studentNumber;
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class LoginStudentResponse {
        private String accessToken;
    }


    @ExceptionHandler(LoginAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleLoginException(LoginAuthenticationException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.UNAUTHORIZED); // 401 상태 코드
    }
}