package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.ReactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "reaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
    @ManyToOne
    private Post post;
    @ManyToOne
    private User user;
    //ama user_id ve post_id bir yerde unique olmalidilar
    @CreationTimestamp
    private LocalDate addTime;
}
