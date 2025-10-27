package register.register.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import register.register.domain.Course;
import register.register.controller.dto.CourseListDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // 비관적 락을 적용하여 Course를 조회하는 메서드
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Course c WHERE c.id = :courseId")
    Optional<Course> findByIdWithPessimisticLock(@Param("courseId") Long courseId);

    @Query("select new register.register.controller.dto.CourseListDto(c.id, c.courseName, c.professor, c.courseCredit, c.currentStudentCapacity, c.maxStudentCapacity, c.courseDaytime) from Course c join c.professor p")
    List<CourseListDto> findCourseList();


}
