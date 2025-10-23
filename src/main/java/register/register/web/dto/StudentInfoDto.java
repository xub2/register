package register.register.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import register.register.domain.Major;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfoDto {

    private String studentNumber;
    private String studentName;
    private Major major;
    private Integer maxCredit;
    private Integer currentCredit;

}
