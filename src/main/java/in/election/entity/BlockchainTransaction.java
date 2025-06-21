package in.election.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "blockchain_transaction", indexes = {
        @Index(name = "idx_tx_hash", columnList = "transaction_hash"),
        @Index(name = "idx_tx_vote", columnList = "vote_id")
})
public class BlockchainTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "transaction_hash", nullable = false, unique = true, length = 64)
    private String transactionHash;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id")
    private BlockchainBlock blockchainBlock;

    @Column(name = "transaction_data", columnDefinition = "TEXT")
    private String transactionData; // JSON of vote data

    @Column(name = "digital_signature", columnDefinition = "TEXT")
    private String digitalSignature;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "gas_used")
    private Long gasUsed;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed = false;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
