package register.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import register.register.domain.Student;
import register.register.web.dto.StudentInfoDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // 학번으로 학생 찾기
    @Query("select s from Student s where s.studentNumber = :studentNumber")
    Optional<Student> findByStudentNumber(@Param("studentNumber") String studentNumber);

    @Query("select new register.register.web.dto.StudentInfoDto(s.studentNumber, s.studentName, s.major, s.maxCredit, s.currentCredit) from Student s join s.major m where s.studentNumber = :studentNumber")
    Optional<StudentInfoDto> findStudentInfoDto(@Param("studentNumber") String studentNumber);

}
