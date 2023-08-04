package com.speedreadingapp.entity;

import com.speedreadingapp.entity.exercise.DisappearNumbersResult;
import com.speedreadingapp.entity.exercise.SpeedMeterResult;
import com.speedreadingapp.util.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "application_user")
public class ApplicationUser implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "application_user_sequence",
            sequenceName = "application_user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "application_user_sequence"
    )
    @Immutable
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "enabled")
    private Boolean enabled = false;

    @Column(name = "account_creation_date")
    private LocalDate accountCreationDate;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "applicationUser", cascade = CascadeType.ALL)
    private Set<DisappearNumbersResult> disappearNumbersResults = new HashSet<>();

    @OneToMany(mappedBy = "applicationUser", cascade = CascadeType.ALL)
    private Set<SpeedMeterResult> speedMeterResults = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));

        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
