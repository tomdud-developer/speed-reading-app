package com.speedreadingapp.service;

import com.speedreadingapp.dto.RegisterRequestDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.exception.UserAlreadyRegisteredException;
import com.speedreadingapp.repository.ApplicationUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<ApplicationUser> optionalUser = applicationUserRepository.findByEmail(email);

        if(optionalUser.isEmpty())
                throw new UsernameNotFoundException(String.format("User with email %s not found", email));
        else return optionalUser.get();
    }

    public String register(RegisterRequestDTO registerRequestDTO) {

        checkIfUserNotAlreadyRegisteredElseThrowException(registerRequestDTO.getEmail());

        ApplicationUser applicationUser = ValueMapper.convertToEntity(registerRequestDTO);

        applicationUser = applicationUserRepository.save(applicationUser);

        return "User has been registered.";
    }

    private void checkIfUserNotAlreadyRegisteredElseThrowException(String email) throws UserAlreadyRegisteredException {
        Optional<ApplicationUser> optionalUser = applicationUserRepository.findByEmail(email);
        if(optionalUser.isPresent())
            throw new UserAlreadyRegisteredException(String.format("User with email %s already registered", email));
    }

}
