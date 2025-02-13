package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "job_application")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User applicant;
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;
    @CreationTimestamp
    private LocalDate applicationDate;
    @Column(nullable = false)
    private String resumeUrl;

    @PrePersist
    protected void OnCreate() {
        this.applicationDate = LocalDate.now();
    }

}
