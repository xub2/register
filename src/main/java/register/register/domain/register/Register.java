package register.register.domain.register;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import register.register.domain.Course;
import register.register.domain.Student;
import register.register.exception.AlreadyCanceledException;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "register", uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_student_course",
                columnNames = {"student_id", "course_id"}
        )
})
public class Register {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "register_status")
    private RegisterStatus registerStatus;

    /**
     * 양방향 메서드
     */

    // 학생 : 수강
    public void setStudent(Student student) {
        this.student = student;
        student.getRegisterList().add(this);
    }

    /**
     * Register 은 User 와 Course 없이는 의미가 없는 객체 -> 정적 팩토리 메서드로 항상 유효한 상태로 생성되어야 함
     */

    public static Register createRegister(Student student, Course course) {
        Register register = new Register();
        register.setStudent(student);
        register.course = course;
        register.registerDate = LocalDateTime.now();
        register.registerStatus = RegisterStatus.COMPLETE;
        return register;
    }

    /**
     * 수강 취소 메서드 캡슐화
     * 먼저 실제 존재하는 데이터가 맞는지 COMPLETE 로 검증
     * 이 메서드는 서비스 계층에서 트랜잭션안에서 호출해서 Dirty Checking으로 데이터가 변경되도록 하기
     */
    public void cancelRegister() {
        // 이미 취소 되었는지 검증
        if (this.registerStatus == RegisterStatus.CANCEL) {
            throw new AlreadyCanceledException("이미 취소 처리된 수강 신청입니다.");
        }

        this.registerStatus = RegisterStatus.CANCEL;
    }

}
