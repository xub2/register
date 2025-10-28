package register.register.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import register.register.domain.Professor;
import register.register.domain.Register;
import register.register.domain.RegisterStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    private Long id;
    private String courseName;
    private Professor professor;
    private Integer courseCredit;
    private LocalDateTime registerDate;
    private RegisterStatus status;

}
