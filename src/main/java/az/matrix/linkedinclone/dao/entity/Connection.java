package az.matrix.linkedinclone.dao.entity;

import az.matrix.linkedinclone.enums.ConnectionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
    Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    User receiver;
    @Enumerated(EnumType.STRING)
    ConnectionStatus status;
    LocalDate sendTime;
    LocalDate responseTime;
}
