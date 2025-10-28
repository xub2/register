package register.register.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import register.register.common.exception.CourseCapacityFullException;
import register.register.common.exception.RegisterClosedException;
import register.register.common.exception.RegisterNotStartedException;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "course")
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_name")
    private String courseName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @Column(name = "course_credit")
    private Integer courseCredit;

    @Column(name = "course_daytime")
    private String courseDaytime;

    @Column(name = "max_student_capacity")
    private Integer maxStudentCapacity;

    // 반드시 내부 메서드만으로 변경이 일어나야한다.
    @Column(name = "current_student_capacity")
    private Integer currentStudentCapacity;

    // 수강 신청 시작 / 종료 시간
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 양 방향 편의 메서드
     */

    // 교수 : 과목
    public void setProfessor(Professor professor) {
        this.professor = professor;
        professor.getCourses().add(this);
    }

    /**
     * 비즈니스 메서드 -> 수강 신청
     * 이 메서드만으로 수강신청 하도록 캡슐화 해야함 (Setter X)
     */
    public void register() {
        LocalDateTime now = LocalDateTime.now();

        // 1. 시간 확인
        if (now.isBefore(startTime)) {
            throw new RegisterNotStartedException("수강 신청 시작 전입니다.");
        }

        if (now.isAfter(endTime)) {
            throw new RegisterClosedException("수강 신청이 마감되었습니다.");
        }

        // 2. 신청 정원 확인
        if (currentStudentCapacity >= maxStudentCapacity) {
            throw new CourseCapacityFullException("수강 신청 정원이 초과되었습니다.");
        }

        // 3. 조건문 다 통과하면 인원 증가
        increaseCapacity();
    }


    /**
     * 수강인원 증가
     */
    public void increaseCapacity() {
        this.currentStudentCapacity++;
    }

    /**
     * 수강 인원 감소
     */
    public void decreaseCapacity() {
        if (this.currentStudentCapacity == null) {
            this.currentStudentCapacity = 0;
        }

        if (this.currentStudentCapacity > 0) {
            this.currentStudentCapacity--;
        }
    }


}
