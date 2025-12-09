package register.register.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import register.register.controller.dto.CartRequest;
import register.register.controller.dto.CartResponse;
import register.register.service.CartService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 담기
     * POST /api/carts
     */
    @PostMapping
    public ResponseEntity<Long> addCart(@RequestBody CartRequest request) {
        Long cartId = cartService.addCart(request.getStudentId(), request.getCourseId());
        return ResponseEntity.ok(cartId);
    }

    /**
     * 장바구니 조회
     * GET /api/carts/{studentId}
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<List<CartResponse>> getCartList(@PathVariable Long studentId) {
        List<CartResponse> cartList = cartService.getCartList(studentId);
        return ResponseEntity.ok(cartList);
    }

    /**
     * 장바구니 삭제
     * DELETE /api/carts/{cartId}
     */
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeCart(@PathVariable Long cartId) {
        cartService.removeCart(cartId);
        return ResponseEntity.ok().build();
    }
}