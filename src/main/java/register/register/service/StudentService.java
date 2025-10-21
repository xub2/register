package register.register.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import register.register.domain.Student;
import register.register.exception.StudentNotFoundException;
import register.register.repository.StudentRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Student findByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber).orElseThrow(() -> new StudentNotFoundException("해당 학생을 찾을 수 없습니다 : " +  studentNumber ));
    }
}
