package register.register.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import register.register.controller.dto.CourseDto;
import register.register.controller.dto.ErrorResponse;
import register.register.service.CourseService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CourseApiController {

    /**
     * [수정] 이 컨트롤러에서 발생 가능한 서버 내부 오류를 처리
     * (예: DB 연결 실패, NullPointerException 등)
     */
    @ExceptionHandler(Exception.class) // 6. SignatureException -> Exception.class
    public ResponseEntity<ErrorResponse> handleCourseException(Exception ex) {
        log.error("CourseApiController에서 예외 발생", ex); // 7. 예외 스택 트레이스 기록

        // 8. 500 Internal Server Error 반환
        return new ResponseEntity<>(
                new ErrorResponse("강의 목록 조회 중 서버 오류가 발생했습니다."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private final CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> getCourses() {
        List<CourseDto> resultCourses = courseService.findAllWithProfessor();
        return ResponseEntity.ok(resultCourses);
    }

}



