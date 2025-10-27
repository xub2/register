package register.register.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import register.register.domain.Student;
import register.register.common.exception.LoginAuthenticationException;
import register.register.repository.StudentRepository;
import register.register.controller.dto.LoginDto;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public Student login(LoginDto loginDto) throws LoginAuthenticationException {

        Student student = studentRepository.findByStudentNumber(loginDto.getStudentNumber())
                .orElseThrow(() -> new LoginAuthenticationException("로그인 시도한 학번("+loginDto.getStudentNumber()+")이 존재하지 않습니다."));

//        if (!student.getPassword().equals(loginDto.getPassword())) {
//            throw new LoginAuthenticationException("학번 또는 비밀번호가 일치하지 않습니다");
//        }

        if (!passwordEncoder.matches(loginDto.getPassword(), student.getPassword())) {
            throw new LoginAuthenticationException("학번 또는 비밀번호가 일치하지 않습니다.");
        }
        return student;
    }
}
