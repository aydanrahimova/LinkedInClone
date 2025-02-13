package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.OrganizationType;
import az.matrix.linkedinclone.enums.EntityStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organization")
@Getter
@Setter
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String logoPath;
    private String overview;
    private String website;
    @Enumerated(EnumType.STRING)
    private EntityStatus status;
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrganizationAdmin> admins = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private OrganizationType organizationType;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.status = EntityStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }
}
