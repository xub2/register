package register.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import register.register.domain.register.Register;
import register.register.web.dto.RegisterDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {

    @Query("select new register.register.web.dto.RegisterDto(r.id, c.courseName, c.professor, c.courseCredit, r.registerDate, r.registerStatus) from Register r join r.course c where r.student.studentNumber = :studentNumber")
    List<RegisterDto> getRegisterByStudentNumber(@Param("studentNumber") String studentNumber);

    @Query("SELECT r FROM Register r JOIN FETCH r.student s JOIN FETCH r.course c WHERE r.id = :registerId")
    Optional<Register> findWithStudentAndCourseById(@Param("registerId") Long registerId);
}

