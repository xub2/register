package register.register.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import register.register.controller.dto.CourseDto;
import register.register.controller.dto.CourseListDto;
import register.register.domain.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE c.id = :courseId")
    Optional<Course> findByCourseId(@Param("courseId") Long courseId);

    @Query("select new register.register.controller.dto.CourseListDto(c.id, c.courseName, c.professor, c.courseCredit, c.currentStudentCapacity, c.maxStudentCapacity, c.courseDaytime) from Course c join c.professor p")
    List<CourseListDto> findCourseList();

    // Professor 엔티티와의 N+1 + DTO로 조회 하도록 리팩토링
    @Query("select new register.register.controller.dto.CourseDto(c.id, c.courseName, p.professorName,c.courseDaytime, c.courseCredit, c.currentStudentCapacity, c.maxStudentCapacity) from Course c join c.professor p")
    List<CourseDto> findAllWithProfessor();

    // X Lock 도입
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Course c where c.id = :courseId")
    Optional<Course> findByIdWithPessimisticLock(@Param("courseId") Long courseId);


}
