package register.register.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import register.register.common.exception.CourseNotFoundException;
import register.register.controller.dto.CourseDto;
import register.register.controller.dto.RegisterApiDto;
import register.register.domain.Course;
import register.register.repository.CourseRepository;
import register.register.controller.dto.CourseListDto;
import register.register.repository.RegisterRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final RegisterRepository registerRepository;

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public Course findByCourseId(Long courseId) {
        return courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new CourseNotFoundException());
    }


    public List<CourseListDto> findCourseList() {
        return courseRepository.findCourseList();
    }

    // N + 1 해결
    public List<CourseDto> findAllWithProfessor() {
        return courseRepository.findAllWithProfessor();
    }

    public List<RegisterApiDto> getRegisterApiDtoByStudentNumber(Long studentId) {
        return registerRepository.getRegisterApiDtoByStudentId(studentId);
    }
}
