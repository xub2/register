package register.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import register.register.domain.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // 학번으로 학생 찾기
    @Query("select s from Student s where s.studentNumber = :studentNumber")
    Optional<Student> findByStudentNumber(@Param("studentNumber") String studentNumber);

}
