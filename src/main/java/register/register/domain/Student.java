package register.register.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    // 학번을 id로 쓰자
    @Column(name = "student_number", unique = true)
    private String studentNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "student_name")
    private String studentName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(name = "max_credit")
    private Integer maxCredit;

    @Column(name = "current_credit")
    private Integer currentCredit;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "student")
    private List<Register> registerList = new ArrayList<>();


    /**
     * 비즈니스 메서드
     */

    // 수강신청하면 과목의 학점 수만큼 현재 학생의 수강 가능 학점 채우기
    public void addCredits(int creditToAdd) {
        if (this.currentCredit == null) {
            this.currentCredit = 0; // null일 경우 0으로 초기화
        }
        int newCredit = this.currentCredit + creditToAdd;
        if (newCredit > this.maxCredit) {
            // 최대 학점 초과 시 예외 처리 (혹은 다른 정책 적용)
            throw new IllegalArgumentException("최대 수강 가능 학점을 초과하였습니다. (신청 학점: " + creditToAdd + ", 현재: " + this.currentCredit + ", 최대: " + this.maxCredit + ")");
        }
        this.currentCredit = newCredit;
    }

    // 수강 취소시 그만큼 학점 빼기
    public void removeCredits(int creditToRemove) {
        if (this.currentCredit == null || this.currentCredit < creditToRemove) {
            // 현재 학점이 null이거나 제거할 학점보다 작으면 오류 (혹은 0으로 처리)
            this.currentCredit = 0; // 또는 예외 발생
        } else {
            this.currentCredit -= creditToRemove;
        }
    }

}
