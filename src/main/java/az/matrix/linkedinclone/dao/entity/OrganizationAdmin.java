package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.OrganizationRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organization_admin")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User admin;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganizationRole role;
}
