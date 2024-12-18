package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.EducationDegree;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "education")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "organization_id",nullable = false)
    private Organization school;
    @Enumerated(EnumType.STRING)
    private EducationDegree degree;
    private String fieldOfStudy;
    private LocalDate startTime;
    private LocalDate endTime;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
