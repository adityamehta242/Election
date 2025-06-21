package in.election.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "voter", indexes = {
        @Index(name = "idx_voter_email", columnList = "email"),
        @Index(name = "idx_voter_phone", columnList = "phone_number"),
        @Index(name = "idx_voter_status", columnList = "status"),
        @Index(name = "idx_voter_created", columnList = "registered_at")
})
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voter_id")
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false, length = 100, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // NEW: Password field for authentication
    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required")
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Column(name = "voter_img")
    private String voterImg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Gender is required")
    private Gender gender;

    @Column(name = "voter_id_proof", length = 50, unique = true)
    private String voterIdProof;

    @Column(name = "voter_proof_img")
    private String voterProofImage;

    @Column(name = "registered_at")
    private LocalDate registeredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoterStatus status = VoterStatus.ACTIVE;

    // Geographic information
    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "country", length = 50)
    private String country = "India";

    @Column(name = "phone_number", length = 15)
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;

    // NEW: Two-Factor Authentication fields
    @Column(name = "two_factor_enabled")
    private Boolean twoFactorEnabled = false;

    @Column(name = "two_factor_secret")
    @JsonIgnore
    private String twoFactorSecret; // TOTP secret key

    @Column(name = "backup_codes", columnDefinition = "TEXT")
    @JsonIgnore
    private String backupCodes; // JSON array of backup codes

    // NEW: Password Reset fields
    @Column(name = "password_reset_token")
    @JsonIgnore
    private String passwordResetToken;

    @Column(name = "password_reset_expires")
    private LocalDateTime passwordResetExpires;

    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts = 0;

    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    @Column(name = "phone_verified")
    private Boolean phoneVerified = false;

    // Existing relationships
    @OneToMany(mappedBy = "voter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "voter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VoterEligibility> eligibilities = new ArrayList<>();

    @OneToMany(mappedBy = "voter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VoterSession> sessions = new ArrayList<>();

    // NEW: Password reset tokens relationship
    @OneToMany(mappedBy = "voter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PasswordResetToken> passwordResetTokens = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (registeredAt == null) {
            registeredAt = LocalDate.now();
        }
    }

    public int getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    // NEW: Account lock check
    public boolean isAccountLocked() {
        return accountLockedUntil != null && LocalDateTime.now().isBefore(accountLockedUntil);
    }
}
