package register.register.controller.dto;

import lombok.Data;
import register.register.domain.Course;

@Data
public class CourseDto {
    private Long id;
    private String courseName;
    private String professorName;
    private String courseDateTime;
    private int courseCredit;
    private int currentStudentCapacity;
    private int maxStudentCapacity;
    private String status;

    // 엔티티 변환용 생성자
    public CourseDto(Course course) {
        this.id = course.getId();
        this.courseName = course.getCourseName();
        this.professorName = course.getProfessor().getProfessorName();
        this.courseDateTime = course.getCourseDaytime();
        this.courseCredit = course.getCourseCredit();
        this.currentStudentCapacity = course.getCurrentStudentCapacity();
        this.maxStudentCapacity = course.getMaxStudentCapacity();
        setStatus(this.currentStudentCapacity, this.maxStudentCapacity);
    }

    // JPQL 조회용 생성자
    public CourseDto(Long id, String courseName, String professorName, String courseDateTime,
                     int courseCredit, int currentStudentCapacity, int maxStudentCapacity) {
        this.id = id;
        this.courseName = courseName;
        this.professorName = professorName;
        this.courseDateTime = courseDateTime;
        this.courseCredit = courseCredit;
        this.currentStudentCapacity = currentStudentCapacity;
        this.maxStudentCapacity = maxStudentCapacity;
        setStatus(currentStudentCapacity, maxStudentCapacity);
    }

    private void setStatus(int current, int max) {
        if (current >= max) {
            this.status = "FULL";
        } else {
            this.status = "AVAILABLE";
        }
    }
}