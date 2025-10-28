package register.register.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import register.register.domain.Register;
import register.register.domain.RegisterStatus;
import register.register.domain.Student;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCreateSuccessApiDto {

    private String studentNumber;
    private Integer currentCredit;
    private Integer maxCredit;
    private Long registerId;
    private String courseName;
    private LocalDateTime registerDate;
    private RegisterStatus status;

    /**
     * [추가] Register 엔티티를 DTO로 변환하는 생성자
     */
    public RegisterCreateSuccessApiDto(Student student, Register register) {
        this.studentNumber = student.getStudentNumber();
        this.currentCredit = student.getCurrentCredit();
        this.maxCredit = student.getMaxCredit();
        this.registerId = register.getId();
        this.courseName = register.getCourse().getCourseName(); // (N+1 주의)
        this.registerDate = register.getRegisterDate();
        this.status = register.getRegisterStatus();
    }
}
