package register.register;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import register.register.domain.Role;
import register.register.domain.Student;
import register.register.common.exception.StudentNotFoundException;
import register.register.repository.StudentRepository;
import register.register.service.StudentService;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EntityBasicTest {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;

    @Test
    public void findStudentByStudentNumber() throws Exception {
        Student findStudent = studentService.findByStudentNumber("student036");
        assertThat(findStudent.getStudentName()).isEqualTo("학생036");
        assertThat(findStudent.getRole()).isEqualTo(Role.STUDENT);
        assertThat(findStudent.getMaxCredit()).isEqualTo(18);
        assertThat(findStudent.getCurrentCredit()).isEqualTo(0);
        assertThat(findStudent.getMajor().getId()).isEqualTo(1);
    }

    @Test
    public void cannotFoundStudent() {
        // 학생은 500명 밖에 없음
        assertThrows(StudentNotFoundException.class, () ->
                studentService.findByStudentNumber("student501"));
    }
}


