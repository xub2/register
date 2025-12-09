package register.register.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import register.register.controller.dto.CartResponse;
import register.register.domain.Cart;
import register.register.domain.Course;
import register.register.domain.Student;
import register.register.repository.CartRepository;
import register.register.repository.CourseRepository;
import register.register.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    /**
     * 장바구니 담기
     */
    @Transactional
    public Long addCart(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));

        // 중복 체크
        if (cartRepository.findByStudentIdAndCourseId(studentId, courseId).isPresent()) {
            throw new IllegalStateException("이미 장바구니에 담긴 강의입니다.");
        }

        // (선택 사항) 이미 수강 신청 완료된 과목인지 체크하는 로직을 여기에 추가할 수도 있음

        Cart cart = Cart.createCart(student, course);
        cartRepository.save(cart);

        return cart.getId();
    }

    /**
     * 장바구니 목록 조회
     */
    public List<CartResponse> getCartList(Long studentId) {
        // 실제로는 페치 조인(fetch join)을 사용한 쿼리를 Repository에 작성하여 N+1 문제를 방지해야 함
        return cartRepository.findByStudentId(studentId).stream()
                .map(CartResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 장바구니 삭제 (취소)
     */
    @Transactional
    public void removeCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 내역이 존재하지 않습니다."));

        cartRepository.delete(cart);
    }
}