package com.speedreadingapp.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.speedreadingapp.configuration.JWTConfigurationProperties;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.exception.AuthoritiesNotMatchWithTokenClaimsException;
import com.speedreadingapp.repository.ApplicationUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JWTTokenService implements JWTManager {

    private final JWTAlgorithmProvider jwtAlgorithmProvider;
    private final JWTConfigurationProperties jwtConfigurationProperties;
    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public String generateAccessToken(Authentication authentication, String issuerUrl) {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * jwtConfigurationProperties.getAccessTokenExpirationInMinutes()))
                .withSubject(authentication.getName())
                .withIssuer(issuerUrl)
                .withClaim("username", authentication.getName())
                .withClaim("roles", retrieveAuthorities(authentication))
                .withClaim("generation-datetime", Instant.now())
                .sign(jwtAlgorithmProvider.getAlgorithm());
    }

    @Override
    public String generateRefreshToken(Authentication authentication, String issuerUrl) {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 30L * 60 * jwtConfigurationProperties.getAccessTokenExpirationInMinutes()))
                .withSubject(authentication.getName())
                .withIssuer(issuerUrl)
                .sign(jwtAlgorithmProvider.getAlgorithm());
    }

    @Override
    public void validate(String token) throws JWTVerificationException, UsernameNotFoundException {
        JWTVerifier verifier = JWT.require(jwtAlgorithmProvider.getAlgorithm()).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        String email = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        UserDetails userDetails = checkIfUserIsInDatabase(email);
        checkClaimsFromTokenWithRolesFromDatabase(userDetails, authorities);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private List<String> retrieveAuthorities(Authentication authentication) {
        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
    }

    private UserDetails checkIfUserIsInDatabase(String email) throws UsernameNotFoundException {
        Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findByEmail(email);

        if(applicationUserOptional.isEmpty())
            throw new UsernameNotFoundException(String.format("User with email %s not found", email));

        return applicationUserOptional.get();
    }

    private void checkClaimsFromTokenWithRolesFromDatabase(UserDetails userDetails, Collection<SimpleGrantedAuthority> authoritiesFromToken)
            throws AuthoritiesNotMatchWithTokenClaimsException {
        Collection<String> authoritiesFromDatabase = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        for (SimpleGrantedAuthority authority : authoritiesFromToken) {
            if (!authoritiesFromDatabase.contains(authority.getAuthority())) {
                throw new AuthoritiesNotMatchWithTokenClaimsException();
            }
        }
    }



}
