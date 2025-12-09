package register.register;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    @PersistenceContext
    private final EntityManager em;

    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initStudent() {
        String sql = """
                INSERT INTO student 
                  (student_number, student_name, password, role, max_credit, current_credit, major_id) 
                VALUES 
                  (:studentNumber, :studentName, :password, :role, :maxCredit, :currentCredit, :majorId)
                """;

        for (int i = 1; i <= 100; i++) {
            //noinspection JpaQueryApiInspection
            em.createNativeQuery(sql)
                    .setParameter("studentNumber", String.format("student%03d", i))
                    .setParameter("studentName", String.format("학생%03d", i))
                    .setParameter("password", encryptPassword("1234"))          // 테스트용
                    .setParameter("role", "STUDENT")       // ENUM 저장 방식이 STRING이면 그대로
                    .setParameter("maxCredit", 18)
                    .setParameter("currentCredit", 0)
                    .setParameter("majorId", 1L)              // major_id
                    .executeUpdate();

            // 대량 삽입시 주기적으로 flush/clear (선택)
            if (i % 50 == 0) {
                em.flush();
                em.clear();
            }
        }

    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}