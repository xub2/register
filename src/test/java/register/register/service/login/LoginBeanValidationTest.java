package register.register.service.login;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import register.register.web.dto.LoginDto;

import java.util.Set;

class LoginBeanValidationTest {

    @Test
    public void loginFormBeanValidationTest() throws Exception {
        //given
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        LoginDto form = new LoginDto();
        form.setStudentNumber("     ");
        form.setPassword("     ");

        //when
        Set<ConstraintViolation<LoginDto>> violations = validator.validate(form);

        //then
        for (ConstraintViolation<LoginDto> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation.message() = " + violation.getMessage());
        }
     }

}