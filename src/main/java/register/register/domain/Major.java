package register.register.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "major")
public class Major {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id")
    private Long id;

    @Column(name = "major_name")
    private String majorName;

    @OneToMany(mappedBy = "major")
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "major")
    private List<Professor> professors = new ArrayList<>();
}
