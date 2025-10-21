package register.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import register.register.domain.Major;

public interface MajorRepository extends JpaRepository<Major, Long> {
}
