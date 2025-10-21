package register.register.service.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import register.register.domain.Student;
import register.register.exception.LoginAuthenticationException;
import register.register.repository.StudentRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final StudentRepository studentRepository;

    public Student login(String studentNumber, String password) throws LoginAuthenticationException {

        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new LoginAuthenticationException("학번이 존재하지 않습니다."));

        if (!student.getPassword().equals(password)) {
            throw new LoginAuthenticationException("학번 또는 비밀번호가 일치하지 않습니다");
        }
        return student;
    }
}
