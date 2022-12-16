package com.src.speedreadingapp.jpa.appuser;


import com.src.speedreadingapp.registration.token.ConfirmationToken;
import com.src.speedreadingapp.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "user %s not found";

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final RoleRepository roleRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username);
                //.orElseThrow(() ->
                   //     new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
        if(user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }

    public String signUpUser(AppUser appUser) {
        boolean isUserExist = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        log.info("Checking email..." + appUser.getEmail());
        if(isUserExist) {
            throw new IllegalStateException("Email is registered!");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
        log.info("Saved user to the database. username = " + appUser.getUsername());

        String token = UUID.randomUUID().toString();
        ConfirmationToken confiramtionToken = new ConfirmationToken(
            token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confiramtionToken);

        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public AppUser getAppUser(String username) {
        return appUserRepository.findByUsername(username);
    }

    public List<AppUser> getUsers() {
        return appUserRepository.findAll();
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public void addRoleToUser(String username, String roleName) {
        Role role = roleRepository.findByName(roleName);
        AppUser user = appUserRepository.findByUsername(username);
        if(user != null) {
            user.getRoles().add(role);
        } else {
            throw new UsernameNotFoundException("User not found");
        }


    }


    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public Optional<AppUser> finById(Long id) {
        return appUserRepository.findById(id);
    }
}
