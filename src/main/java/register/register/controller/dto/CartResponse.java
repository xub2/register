package register.register.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import register.register.domain.Cart;

@Data
@AllArgsConstructor
public class CartResponse {

    private Long cartId;
    private Long courseId;
    private String courseName;
    private String professorName;
    private Integer credit;

    public static CartResponse from(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getCourse().getId(),
                cart.getCourse().getCourseName(),
                cart.getCourse().getProfessor().getProfessorName(),
                cart.getCourse().getCourseCredit()
        );
    }

}
