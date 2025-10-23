package register.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import register.register.domain.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
