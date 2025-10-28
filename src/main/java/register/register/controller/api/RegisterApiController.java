package register.register.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import register.register.common.exception.AlreadyCanceledException;
import register.register.common.exception.CourseCapacityFullException;
import register.register.common.exception.RegisterClosedException;
import register.register.common.exception.RegisterNotStartedException;
import register.register.controller.dto.ErrorResponse;
import register.register.controller.dto.RegisterApiDto;
import register.register.controller.dto.RegisterCreateSuccessApiDto;
import register.register.controller.dto.RegistrationRequest;
import register.register.domain.Register;
import register.register.domain.Student;
import register.register.service.CourseService;
import register.register.service.RegisterService;
import register.register.service.StudentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class RegisterApiController {

    private final RegisterService registerService;
    private final StudentService studentService;
    private final CourseService courseService;


    @PostMapping("/registrations")
    public ResponseEntity<RegisterCreateSuccessApiDto> createRegister(
            @RequestBody RegistrationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        Student student = studentService.findByStudentNumber(userDetails.getUsername());
        Register register = registerService.createRegister(student, request.getCourseId());

        RegisterCreateSuccessApiDto registerCreateSuccessApiDto = new RegisterCreateSuccessApiDto(student,register);

        return new ResponseEntity<>(registerCreateSuccessApiDto,HttpStatus.CREATED);

    }

    @GetMapping("/registrations")
    public ResponseEntity<List<RegisterApiDto>> findAllRegister(@AuthenticationPrincipal UserDetails userDetails) {
        Long studentId = studentService.findByStudentNumber(userDetails.getUsername()).getId();
        List<RegisterApiDto> registerApiDtoByStudentNumber = courseService.getRegisterApiDtoByStudentNumber(studentId);

        return new ResponseEntity<>(registerApiDtoByStudentNumber, HttpStatus.OK);
    }

    @DeleteMapping("/registrations/{registerId}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable("registerId") Long registerId) {
        registerService.cancelRegister(registerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /**
     * [수정 3] 예외 핸들러 (400 Bad Request)
     * - 사용자의 요청이 비즈니스 규칙에 맞지 않을 때
     */
    @ExceptionHandler({
            IllegalArgumentException.class,     // 학점 초과, 잘못된 ID
            CourseCapacityFullException.class,  // 정원 마감
            RegisterNotStartedException.class,  // 시작 전
            RegisterClosedException.class       // 마감
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex) {
        log.warn("수강 신청/취소 실패 (400 Bad Request): {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * [수정 3] 예외 핸들러 (409 Conflict)
     * - DB 상태와 충돌이 날 때 (중복 신청, 이미 취소됨)
     */
    @ExceptionHandler({
            DataIntegrityViolationException.class, // 이미 신청한 과목 (Unique 제약조건)
            AlreadyCanceledException.class         // 이미 취소된 내역
    })
    public ResponseEntity<ErrorResponse> handleConflictException(Exception ex) {
        String errorMessage;
        if (ex instanceof DataIntegrityViolationException) {
            errorMessage = "이미 수강 신청한 과목입니다.";
        } else {
            errorMessage = ex.getMessage(); // "이미 취소 처리된 수강 신청입니다."
        }

        log.warn("수강 신청/취소 실패 (409 Conflict): {}", errorMessage);
        ErrorResponse response = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
