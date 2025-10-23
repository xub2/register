package register.register.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import register.register.domain.Course;
import register.register.repository.CourseRepository;
import register.register.web.dto.CourseListDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> courses() {
        return courseRepository.findAll();
    }

    public List<CourseListDto> findCourseList() {
        return courseRepository.findCourseList();
    }
}
