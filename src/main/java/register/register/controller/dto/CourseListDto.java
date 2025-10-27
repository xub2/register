package register.register.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import register.register.domain.Professor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseListDto {

    private Long id;
    private String courseName;
    private Professor professor;
    private Integer courseCredit;
    private Integer currentStudentCapacity;
    private Integer maxStudentCapacity;
    private String courseDayTime;

}
