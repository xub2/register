package register.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RegisterApplication {

    // 시작전 github repo secret 에 EC2 엔드포인트 수정

    public static void main(String[] args) {
        System.out.println("가보자~ N+N+N트째");
        SpringApplication.run(RegisterApplication.class, args);
    }

}
