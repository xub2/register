package register.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import register.register.domain.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByStudentId(Long studentId);

    Optional<Cart> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
