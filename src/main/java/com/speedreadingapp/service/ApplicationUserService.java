package com.speedreadingapp.service;

import com.speedreadingapp.dto.RegisterRequestDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.repository.ApplicationUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getApplicationUser(email);
    }

    public ApplicationUser getApplicationUser(String email) throws UsernameNotFoundException {

        Optional<ApplicationUser> optionalUser = applicationUserRepository.findByEmail(email);

        if(optionalUser.isEmpty())
            throw new UsernameNotFoundException(String.format("User with email %s not found", email));

        return optionalUser.get();
    }

    public String register(RegisterRequestDTO registerRequestDTO) {

        registerRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        ApplicationUser applicationUser = ValueMapper.convertToEntity(registerRequestDTO);

        applicationUserRepository.save(applicationUser);

        return "User has been registered.";
    }

    public ApplicationUser getUserFromAuthContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email;
        if (principal instanceof User user)
            email = user.getUsername();
        else if (principal instanceof String string)
            email = string;
        else
            throw new SecurityException("Unsupported principal in context");

        return this.getApplicationUser(email);
    }



}
