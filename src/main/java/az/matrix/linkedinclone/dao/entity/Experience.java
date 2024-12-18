package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.EmploymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "experience")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "organization_id",nullable = false)
    private Organization company;
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;
    private LocalDate startTime;
    private LocalDate endTime;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
