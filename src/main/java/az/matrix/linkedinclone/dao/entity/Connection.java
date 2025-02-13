package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.ConnectionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "connection")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @Enumerated(EnumType.STRING)
    private ConnectionStatus status;
    private LocalDateTime sendTime;
    private LocalDateTime responseTime;

    @PrePersist
    protected void onCreate() {
        this.sendTime = LocalDateTime.now();
    }

}
