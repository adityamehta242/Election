package in.election.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id" , nullable = false)
    private Long partyId;

    @Column(name = "party_name", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Party name is required")
    private String partyName;

    @Column(name = "party_motto", length = 200)
    private String partyMotto;

    @Column(name = "party_description", length = 1000)
    private String partyDescription;

    @Column(name = "symbol", length = 100)
    private String symbol;

    @Column(name = "founded_date") // Better name than partyDOB
    private LocalDate foundedDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Add audit fields
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Candidate> candidates = new ArrayList<>();

    // Add this to Party.java
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ElectionCandidate> electionCandidates = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
