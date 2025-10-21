package register.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import register.register.domain.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
