package register.register.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatusPageController {

    @GetMapping("/application-unavailable")
    public String unavailablePage(@RequestParam(value = "msg", defaultValue = "현재 이용 가능한 시간이 아닙니다.") String msg, Model model) {

        // 1. 파라미터로 받은 msg를 Model에 담아 HTML로 전달
        model.addAttribute("alertMessage", msg);

        return "application-unavailable"; // templates/application-unavailable.html
    }
}