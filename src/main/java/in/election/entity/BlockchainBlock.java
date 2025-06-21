package in.election.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "blockchain_block", indexes = {
        @Index(name = "idx_block_hash", columnList = "block_hash"),
        @Index(name = "idx_block_number", columnList = "block_number"),
        @Index(name = "idx_block_timestamp", columnList = "timestamp")
})
public class BlockchainBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_number", nullable = false, unique = true)
    private Long blockNumber;

    @Column(name = "block_hash", nullable = false, unique = true, length = 64)
    private String blockHash;

    @Column(name = "previous_hash", length = 64)
    private String previousHash;

    @Column(name = "merkle_root", nullable = false, length = 64)
    private String merkleRoot;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "nonce")
    private Long nonce;

    @Column(name = "difficulty")
    private Integer difficulty;

    @Column(name = "vote_count")
    private Integer voteCount;

    @Column(name = "validator", length = 100)
    private String validator; // Admin who validated the block

    @Column(name = "is_valid")
    private Boolean isValid = true;

    // Current issue: BlockchainTransaction → BlockchainBlock is correct
    // But BlockchainBlock should have proper cascade for transactions
    @OneToMany(mappedBy = "blockchainBlock", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<BlockchainTransaction> transactions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}

