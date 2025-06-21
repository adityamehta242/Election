package in.election.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "voter_eligibility" , uniqueConstraints = @UniqueConstraint(columnNames = {"voter_id", "election_id"}) , indexes = {
        @Index(name = "idx_eligibility_election_voter", columnList = "election_id, voter_id")
})
public class VoterEligibility {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "voter_id", nullable = false)
    private Voter voter;

    @ManyToOne
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    @Column(name = "is_eligible", nullable = false)
    private Boolean isEligible;

    @Column(name = "eligibility_reason")
    private String eligibilityReason; // Why eligible/not eligible

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    @Column(name = "has_voted", nullable = false)
    private Boolean hasVoted = false;

    @Column(name = "voted_at")
    private LocalDateTime votedAt;

    @AssertTrue(message = "Voter must meet age requirements")
    public boolean meetsAgeRequirements() {
        if (voter == null || election == null) return true;
        int voterAge = voter.getAge();
        return voterAge >= election.getMinAge() &&
                (election.getMaxAge() == null || voterAge <= election.getMaxAge());
    }

}
