package register.register.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import register.register.controller.dto.ApiResponseDto;
import register.register.domain.Student;
import register.register.exception.AlreadyCanceledException;
import register.register.service.CourseService;
import register.register.service.RegisterService;
import register.register.service.StudentService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/courses")
@Slf4j
public class CourseController {

    private final CourseService courseService;
    private final StudentService studentService;
    private final RegisterService registerService;

    @GetMapping
    public String courses(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        String studentNumber = userDetails.getUsername();

        model.addAttribute("studentInfo", studentService.getStudentInfo(studentNumber));
        model.addAttribute("courses", courseService.findCourseList());

        return "courses";
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponseDto> enroll(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody CourseIdDto courseIdDto) {
        try {
            String studentNumber = userDetails.getUsername();
            Student findStudent = studentService.findByStudentNumber(studentNumber);

            Long courseId = courseIdDto.getCourseId();

            // 실제 등록 처리: 서비스에 위임 (코스 아이디를 넘겨 내부에서 조회/검증)
            registerService.createRegister(findStudent, courseId);

            return ResponseEntity.ok(new ApiResponseDto("신청에 성공하였습니다."));

        } catch (IllegalArgumentException e) {
            // [수정] 최대 학점 초과 (Student.addCredits 에서 발생) 예외 처리
            log.warn("Enrollment failed due to validation error (e.g., max credits): {}", e.getMessage());
            // 400 Bad Request 상태 코드와 예외 메시지를 그대로 전달
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400 상태 코드
                    .body(new ApiResponseDto(e.getMessage())); // 예외 메시지를 body에 담음

        } catch (Exception e) {
            log.error("Unexpected error during enrollment for student {} and course {}: {}",
                    (userDetails != null ? userDetails.getUsername() : "UNKNOWN"),
                    courseIdDto, // Use the assigned courseId
                    e.getMessage(),
                    e);

            // 500 Internal Server Error
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto("수강 신청 중 서버 오류가 발생했습니다. 관리자에게 문의해주세요."));
        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CourseIdDto {
        private Long courseId;
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponseDto> cancel(@RequestBody RegisterIdDto registerIdDto) {
        try {
            // 서비스에 registerId를 넘겨 취소 로직 실행
            registerService.cancelRegister(registerIdDto.getRegisterId());

            return ResponseEntity.ok(new ApiResponseDto("수강 신청이 정상적으로 취소되었습니다."));

        } catch (AlreadyCanceledException | IllegalArgumentException e) {
            // 이미 취소된 경우 (400 Bad Request)
            log.warn("Cancellation failed due to validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(e.getMessage()));

        } catch (Exception e) {
            // 기타 서버 오류 (500 Internal Server Error)
            log.error("Unexpected error during cancellation for registerId {}: {}",
                    registerIdDto.getRegisterId(),
                    e.getMessage(),
                    e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto("수강 취소 중 서버 오류가 발생했습니다."));
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class RegisterIdDto {
        private Long registerId;
    }

}
