package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.EmploymentType;
import az.matrix.linkedinclone.enums.EntityStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
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
    private List<Skill> skills = new ArrayList<>();
    @CreationTimestamp
    private LocalDate postedAt;
    private LocalDate applicationDeadline;
    @Enumerated(EnumType.STRING)
    private EntityStatus status;
    //skillar her hansisa usera uyursa onda ona maile mesaj gelir ki iw uygundur ona
}

