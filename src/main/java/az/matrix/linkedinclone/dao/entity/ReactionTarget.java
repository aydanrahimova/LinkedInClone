package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.AuthorType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reaction_target")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ReactionTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthorType authorType;
    @Column(nullable = false)
    private Long authorId;
    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions;
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

