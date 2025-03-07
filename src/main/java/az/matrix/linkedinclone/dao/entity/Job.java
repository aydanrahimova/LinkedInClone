package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.EmploymentType;
import az.matrix.linkedinclone.enums.EntityStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @JoinTable(
            name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();
    @CreationTimestamp
    private LocalDateTime postedAt;
    private LocalDate applicationDeadline;
    @Enumerated(EnumType.STRING)
    private EntityStatus status;
}

