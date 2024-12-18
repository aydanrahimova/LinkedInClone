package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.ConnectionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "connection")
@Getter
@Setter
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User sender;
    @ManyToOne
    private User receiver;
    @Enumerated(EnumType.STRING)
    private ConnectionStatus status;
    private LocalDate sendTime;
    private LocalDate responseTime;
}
