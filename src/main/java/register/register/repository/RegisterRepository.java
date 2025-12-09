package register.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import register.register.controller.dto.RegisterApiDto;
import register.register.domain.Register;
import register.register.controller.dto.RegisterDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {

    @Query("select new register.register.controller.dto.RegisterDto(r.id, c.courseName, c.professor, c.courseCredit, r.registerDate, r.registerStatus) from Register r join r.course c where r.student.studentNumber = :studentNumber")
    List<RegisterDto> getRegisterByStudentNumber(@Param("studentNumber") String studentNumber);

    @Query("select new register.register.controller.dto.RegisterApiDto(r.id, c.courseName, p.professorName, r.registerStatus) " +
            "from Register r " +
            "join r.course c " +
            "join c.professor p " +
            "where r.student.id = :id")
    List<RegisterApiDto> getRegisterApiDtoByStudentId(@Param("id") Long studentId);

    @Query("select r from Register r join fetch r.student s join fetch r.course c where r.id = :registerId")
    Optional<Register> findWithStudentAndCourseById(@Param("registerId") Long registerId);
}

