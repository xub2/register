package register.register.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import register.register.domain.Register;
import register.register.domain.RegisterStatus;

@Data
@NoArgsConstructor
public class RegisterApiDto {

    private Long registerId;
    private String courseName;
    private String professorName;
    private RegisterStatus status;

    public RegisterApiDto(Long registerId, String courseName, String professorName, RegisterStatus status) {
        this.registerId = registerId;
        this.courseName = courseName;
        this.professorName = professorName;
        this.status = status;
    }

    public RegisterApiDto(Register register) {
        this.registerId = register.getId();
        this.courseName = register.getCourse().getCourseName();
        this.professorName = register.getCourse().getProfessor().getProfessorName();
        this.status = register.getRegisterStatus();
    }
}
