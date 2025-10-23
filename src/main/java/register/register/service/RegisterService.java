package register.register.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import register.register.repository.RegisterRepository;
import register.register.web.dto.RegisterDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegisterService {

    private final RegisterRepository registerRepository;

    public List<RegisterDto> getStudentRegisters(String studentNumber) {
        return registerRepository.getRegisterByStudentNumber(studentNumber);
    }
}
