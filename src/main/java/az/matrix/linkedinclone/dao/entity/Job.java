package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.EmploymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "job")
@Getter
@Setter
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;
    private String description;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @ManyToMany
    private List<Skill> skills;
    private String email;//hara ki cv'ler gelecek
    @CreationTimestamp
    private LocalDate postedAt;
    private LocalDate applicationDeadline;
    //skillar her hansisa usera uyursa onda ona maile mesaj gelir ki iw uygundur ona
}

