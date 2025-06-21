package in.election.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id" , nullable = false)
    private Long candidateId;

    @NotBlank(message = "Candidate name is required")
    @Column(name = "candidate_name" , nullable = false , length = 100)
    private String candidateName;

    @Column(name = "candidate_description" , nullable = false, length = 500)
    private String candidateDescription;

    @Column(name = "candidate_image")
    private String candidateImage;

    @ManyToOne(fetch = FetchType.LAZY) // Add fetch type
    @JoinColumn(name = "party_id", nullable = false) // Make party mandatory
    @NotNull(message = "Party is required")
    private Party party; // Renamed from partyId to party for clarity

    // Add audit fields
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Add this to Candidate.java
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ElectionCandidate> electionCandidates = new ArrayList<>();

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
}
