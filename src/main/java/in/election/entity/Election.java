package in.election.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "election", indexes = {
        @Index(name = "idx_election_status", columnList = "status"),
        @Index(name = "idx_election_type", columnList = "type"),
        @Index(name = "idx_election_dates", columnList = "start_date, end_date"),
        @Index(name = "idx_election_created", columnList = "created_at")
})
public class Election {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "election_id")
    private Long electionId;

    @NotBlank(message = "Election name is required")
    @Column(nullable = false, length = 200)
    private String name;

    @NotBlank(message = "Election description is required")
    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "start_date", nullable = false)
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @Column(name = "election_motto", length = 500)
    private String electionMotto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Election status is required")
    private ElectionStatus status = ElectionStatus.SCHEDULED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Election type is required")
    private ElectionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    private Candidate winner;

    // Eligibility criteria
    @Column(name = "min_age")
    @Min(value = 18, message = "Minimum age cannot be less than 18")
    private Integer minAge = 18;

    @Column(name = "max_age")
    @Max(value = 120, message = "Maximum age cannot exceed 120")
    private Integer maxAge;

    @Column(name = "allowed_genders", length = 100)
    private String allowedGenders;

    @Column(name = "geographic_restriction", length = 200)
    private String geographicRestriction;

    @Column(name = "voter_status_required", length = 20)
    private String voterStatusRequired = "ACTIVE";

    // NEW: Real-time tracking fields
    @Column(name = "total_registered_voters")
    private Long totalRegisteredVoters = 0L;

    @Column(name = "total_votes_cast")
    private Long totalVotesCast = 0L;

    @Column(name = "turnout_percentage", precision = 5, scale = 2)
    private BigDecimal turnoutPercentage = BigDecimal.ZERO;

    @Column(name = "last_result_update")
    private LocalDateTime lastResultUpdate;

    @Column(name = "blockchain_enabled")
    private Boolean blockchainEnabled = true;

    @Column(name = "real_time_results_enabled")
    private Boolean realTimeResultsEnabled = true;

    // Audit fields
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @AssertTrue(message = "Election must have at least one candidate")
    public boolean hasMinimumCandidates() {
        // Only validate if election is not in SCHEDULED status
        return status == ElectionStatus.SCHEDULED ||
                (electionCandidates != null && electionCandidates.size() >= 2);
    }

    // Relationships
    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ElectionCandidate> electionCandidates = new ArrayList<>();

    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VoterEligibility> voterEligibilities = new ArrayList<>();

    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ElectionResult> results = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @AssertTrue(message = "End date must be after start date")
    public boolean isEndDateAfterStartDate() {
        return endDate == null || startDate == null || endDate.isAfter(startDate);
    }
}
