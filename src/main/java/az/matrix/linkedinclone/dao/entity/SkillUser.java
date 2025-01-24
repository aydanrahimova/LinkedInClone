package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "skill_user")
@Getter
@Setter
public class SkillUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private SkillLevel level;
}
