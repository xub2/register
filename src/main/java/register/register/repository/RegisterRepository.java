package register.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import register.register.domain.register.Register;

public interface RegisterRepository extends JpaRepository<Register, Long> {
}
