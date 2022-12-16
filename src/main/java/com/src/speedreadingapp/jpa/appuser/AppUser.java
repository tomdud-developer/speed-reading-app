package com.src.speedreadingapp.jpa.appuser;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.src.speedreadingapp.jpa.pdfuser.PdfUser;
import com.src.speedreadingapp.jpa.schultzarraylogs.SchultzArrayLog;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLog;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static javax.persistence.FetchType.*;

//@JsonIdentityInfo(
      //   generator = ObjectIdGenerators.PropertyGenerator.class,
//)
@EqualsAndHashCode
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUser implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "appuser_sequence",
            sequenceName = "appuser_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "appuser_sequence"
    )
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private Boolean locked = false;
    private Boolean enabled = false;
    @JoinTable(
            name = "app_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ManyToMany(fetch = EAGER, cascade = CascadeType.ALL)
    private Set<Role> roles;

    @OneToMany
    @JsonManagedReference
    private Set<SpeedMeterLog> speedMeterLogs;

    @OneToOne
    private PdfUser pdfUser;

    @OneToOne
    private SchultzArrayLog schultzArrayLog;




    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(roles != null)
            roles.forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
