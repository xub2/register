package register.register.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cart", uniqueConstraints = {
        @UniqueConstraint(name = "unique_cart_student_course",
                columnNames = {"student_id", "course_id"}
        )
})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public static Cart createCart(Student student, Course course) {
        Cart cart = new Cart();
        cart.student = student;
        cart.course = course;
        cart.createdAt = LocalDateTime.now();
        return cart;
    }
}
