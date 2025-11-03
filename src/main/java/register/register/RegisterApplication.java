package register.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RegisterApplication {

    public static void main(String[] args) {
        System.out.println("오늘은 잘될거야 N+1트째");
        SpringApplication.run(RegisterApplication.class, args);
    }

}
