package register.register.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotEmpty
    private String studentNumber;

    @NotEmpty
    private String password;
}
