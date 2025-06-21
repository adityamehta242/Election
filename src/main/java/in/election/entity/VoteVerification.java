package in.election.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "vote_verification")
public class VoteVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    @Column(name = "verification_code", unique = true)
    private String verificationCode;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "verification_method")
    private String verificationMethod;
}
