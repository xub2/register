package register.register.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import register.register.domain.Student;
import register.register.common.exception.StudentNotFoundException;
import register.register.repository.StudentRepository;
import register.register.controller.dto.StudentInfoDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Student findByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber).orElseThrow(() -> new StudentNotFoundException("해당 학생을 찾을 수 없습니다 : " +  studentNumber ));
    }

    // 이 메서드는 JWT 토큰이 있는 상태에서만 접근 가능하기 때문에 .get() 써도 될 듯?
    public StudentInfoDto getStudentInfo(String studentNumber) {
        return studentRepository.findStudentInfoDto(studentNumber).get();
    }
}
