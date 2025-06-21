package in.election.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "vote",
        indexes = {
                @Index(name = "idx_vote_election", columnList = "election_id"),
                @Index(name = "idx_vote_voter", columnList = "voter_id"),
                @Index(name = "idx_vote_candidate", columnList = "candidate_id"),
                @Index(name = "idx_vote_date", columnList = "voting_date"),
                @Index(name = "idx_vote_hash", columnList = "blockchain_hash"),
                @Index(name = "idx_vote_election_candidate", columnList = "election_id, candidate_id")
        },
        uniqueConstraints = @UniqueConstraint(columnNames = {"voter_id", "election_id"}))
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id", nullable = false)
    @NotNull(message = "Voter is required")
    private Voter voter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id", nullable = false)
    @NotNull(message = "Election is required")
    private Election election;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    @NotNull(message = "Candidate is required")
    private Candidate candidate;

    @Column(name = "voting_date", nullable = false)
    @NotNull(message = "Voting date is required")
    private LocalDateTime votingDate;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "verification_method", length = 50)
    private String verificationMethod;

    // NEW: Blockchain integration fields
    @Column(name = "blockchain_hash", length = 64, unique = true)
    private String blockchainHash; // SHA-256 hash

    @Column(name = "previous_hash", length = 64)
    private String previousHash;

    @Column(name = "nonce")
    private Long nonce;

    @Column(name = "block_timestamp")
    private LocalDateTime blockTimestamp;

    @Column(name = "blockchain_verified")
    private Boolean blockchainVerified = false;

    @Column(name = "merkle_root", length = 64)
    private String merkleRoot;

    // NEW: Vote verification
    @Column(name = "digital_signature", columnDefinition = "TEXT")
    private String digitalSignature;

    @Column(name = "vote_encrypted", columnDefinition = "TEXT")
    private String voteEncrypted; // Encrypted vote data

    @AssertTrue(message = "Vote must be cast during election period")
    public boolean isVotedDuringElectionPeriod() {
        if (election == null || votingDate == null) return true;
        return votingDate.isAfter(election.getStartDate()) &&
                votingDate.isBefore(election.getEndDate());
    }

    @PrePersist
    protected void onCreate() {
        if (votingDate == null) {
            votingDate = LocalDateTime.now();
        }
        if (blockTimestamp == null) {
            blockTimestamp = LocalDateTime.now();
        }
    }
}
