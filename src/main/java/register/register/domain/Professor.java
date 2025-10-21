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
@Table(name = "professor")
public class Professor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "professor_id")
    private Long id;

    @Column(name = "professor_name")
    private String professorName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(name = "laboratory")
    private String laboratory;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    // Course와 양방향 연관관계
    @OneToMany(mappedBy = "professor")
    private List<Course> courses = new ArrayList<>();
}
