package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.ReactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reaction")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "target_id")
    private ReactionTarget target;
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
    @CreationTimestamp
    private LocalDateTime addTime;
    private LocalDateTime editTime;
    private Boolean isEdited;

    @PrePersist
    protected void onCreate() {
        this.addTime = LocalDateTime.now();
        this.isEdited = Boolean.FALSE;
    }

    @PreUpdate
    protected void onUpdate() {
        this.editTime = LocalDateTime.now();
        this.isEdited = Boolean.TRUE;
    }
}
