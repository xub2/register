package register.register.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import register.register.controller.dto.RegisterDto;
import register.register.domain.Course;
import register.register.domain.Register;
import register.register.domain.Student;
import register.register.repository.CourseRepository;
import register.register.repository.RegisterRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegisterService {

    private final CourseRepository courseRepository;
    private final RegisterRepository registerRepository;

    public List<RegisterDto> getStudentRegisters(String studentNumber) {
        return registerRepository.getRegisterByStudentNumber(studentNumber);
    }

    @Transactional
    public Register createRegister(Student student, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 과목 ID 입니다."));
        Register register = Register.createRegister(student, course);

        course.increaseCapacity();
        student.addCredits(course.getCourseCredit());

        return registerRepository.save(register);
    }

    @Transactional
    public void cancelRegister(Long registerId) {
        // registerId로 Register 엔티티를 조회 (Student, Course 정보가 필요하므로 fetch join)
        Register register = registerRepository.findWithStudentAndCourseById(registerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 수강 신청 내역입니다. ID: " + registerId));

        Student student = register.getStudent();
        Course course = register.getCourse();

        register.cancelRegister(); // (Register 엔티티의 메서드 시그니처 수정 권장 - 4번 항목 참고)
        student.removeCredits(course.getCourseCredit());
        course.decreaseCapacity();
    }
}
