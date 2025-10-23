package register.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import register.register.domain.Major;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
}
