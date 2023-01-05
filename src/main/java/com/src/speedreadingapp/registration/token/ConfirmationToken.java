package com.src.speedreadingapp.registration.token;

import com.src.speedreadingapp.jpa.appuser.AppUser;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class ConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "confirmationtoken_sequence",
            sequenceName = "confirmationtoken_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmationtoken_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column()
    private LocalDateTime comfirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public ConfirmationToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiresAt,
                             AppUser appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.appUser = appUser;
    }

    public ConfirmationToken() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getComfirmedAt() {
        return comfirmedAt;
    }

    public AppUser getAppUser() {
        return appUser;
    }

}
