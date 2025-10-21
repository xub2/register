package register.register.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import register.register.domain.register.Register;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "student")
public class Student {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
