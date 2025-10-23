package register.register.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import register.register.service.CourseService;
import register.register.service.StudentService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/courses")
@Slf4j
public class CourseController {

    private final CourseService courseService;
    private final StudentService studentService;

    @GetMapping
    public String courses(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        String studentNumber = userDetails.getUsername();

        model.addAttribute("studentInfo", studentService.getStudentInfo(studentNumber));
        model.addAttribute("courses", courseService.findCourseList());

        return "courses";
    }
}
