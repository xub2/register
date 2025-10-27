package register.register.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import register.register.domain.Course;
import register.register.domain.Student;
import register.register.domain.register.Register;
import register.register.repository.CourseRepository;
import register.register.repository.RegisterRepository;
import register.register.repository.StudentRepository;
import register.register.web.dto.RegisterDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegisterService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final RegisterRepository registerRepository;

    public List<RegisterDto> getStudentRegisters(String studentNumber) {
        return registerRepository.getRegisterByStudentNumber(studentNumber);
    }

    @Transactional
    public void createRegister(Student student, Long courseId) {
        Course course = courseRepository.findById(courseId).get();
        Register register = Register.createRegister(student, course);

        course.increaseCapacity();
        student.addCredits(course.getCourseCredit());

        registerRepository.save(register);

    }

    @Transactional
    public void cancelRegister(Long registerId) {
        // 1. registerId로 Register 엔티티를 조회 (Student, Course 정보가 필요하므로 fetch join)
        // (RegisterRepository에 findWithStudentAndCourseById 메서드 추가 필요 - 3번 항목 참고)
        Register register = registerRepository.findWithStudentAndCourseById(registerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 수강 신청 내역입니다. ID: " + registerId));

        Student student = register.getStudent();
        Course course = register.getCourse();

        // 2. 도메인 메서드 호출 (상태 변경 및 중복 취소 검증)
        register.cancelRegister(); // (Register 엔티티의 메서드 시그니처 수정 권장 - 4번 항목 참고)

        // 3. 학생 학점 복구 (Student.java의 removeCredits 호출)
        student.removeCredits(course.getCourseCredit());

        // 4. 과목 현재 정원 복구 (Course.java에 decreaseCapacity 메서드 추가 필요 - 5번 항목 참고)
        course.decreaseCapacity();
    }
}
