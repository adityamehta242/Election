package in.election.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "recipient_id", nullable = false)
    private String recipientId; // Voter ID or Admin ID

    @Column(name = "recipient_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType recipientType;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Column(name = "notification_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "related_entity_id")
    private String relatedEntityId; // Election ID, Vote ID, etc.

    @Column(name = "related_entity_type")
    private String relatedEntityType;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
