package in.election.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@Table(name = "election_candidate",
        uniqueConstraints = @UniqueConstraint(columnNames = {"election_id", "candidate_id"}))
public class ElectionCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "registration_status")
    @Enumerated(EnumType.STRING)
    private CandidateRegistrationStatus status = CandidateRegistrationStatus.APPROVED;

    @Column(name = "ballot_position")
    private Integer ballotPosition;

    // Unique constraint to prevent duplicate candidate registrations
    // Add PrePersist
    @PrePersist
    protected void onCreate() {
        if (registeredAt == null) {
            registeredAt = LocalDateTime.now();
        }
    }
}