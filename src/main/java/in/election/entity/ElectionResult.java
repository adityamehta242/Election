package in.election.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "election_results" , uniqueConstraints = @UniqueConstraint(columnNames = {"election_id", "candidate_id"}) ,indexes = {
        @Index(name = "idx_result_election_votes", columnList = "election_id, vote_count")
})
public class ElectionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column(name = "vote_count", nullable = false)
    private Long voteCount;

    @Column(name = "percentage")
    private Double percentage;

    @Column(name = "rank_position")
    private Integer rankPosition;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}
