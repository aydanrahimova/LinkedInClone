package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.AuthorType;
import az.matrix.linkedinclone.enums.ReactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

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

    private Long authorId;
    @Enumerated(EnumType.STRING)
    private AuthorType authorType;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
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
