package com.speedreadingapp.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
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
public class JWTTokenService implements JWTManager {


    private final JWTConfigurationProperties jwtConfigurationProperties;
    private final ApplicationUserRepository applicationUserRepository;
    private final JWTVerifier verifier;
    private final Algorithm algorithm;

    public static final String TOKEN_TYPE_CLAIM_NAME = "token-type";

    public JWTTokenService(
            JWTAlgorithmProvider jwtAlgorithmProvider,
            JWTConfigurationProperties jwtConfigurationProperties,
            ApplicationUserRepository applicationUserRepository) {
        this.jwtConfigurationProperties = jwtConfigurationProperties;
        this.applicationUserRepository = applicationUserRepository;
        this.algorithm = jwtAlgorithmProvider.getAlgorithm();
        this.verifier = JWT.require(this.algorithm).build();
    }

    @Override
    public String generateAccessTokenBasedOnRefreshToken(String refreshToken) {
        validate(refreshToken);
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        UserDetails userDetails = checkIfUserIsInDatabase(decodedJWT.getSubject());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null);
        return generateAccessToken(usernamePasswordAuthenticationToken, decodedJWT.getIssuer());
    }
    
    @Override
    public String generateAccessToken(Authentication authentication, String issuerUrl) {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * jwtConfigurationProperties.getAccessTokenExpirationInMinutes()))
                .withSubject(authentication.getName())
                .withIssuer(issuerUrl)
                .withClaim("username", authentication.getName())
                .withClaim("roles", retrieveAuthorities(authentication))
                .withClaim("generation-datetime", Instant.now())
                .withClaim(TOKEN_TYPE_CLAIM_NAME, "access_token")
                .sign(algorithm);
    }

    @Override
    public String generateRefreshToken(Authentication authentication, String issuerUrl) {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 30L * 60 * jwtConfigurationProperties.getAccessTokenExpirationInMinutes()))
                .withSubject(authentication.getName())
                .withIssuer(issuerUrl)
                .withClaim(TOKEN_TYPE_CLAIM_NAME, "refresh_token")
                .sign(algorithm);
    }

    @Override
    public void validate(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = verifier.verify(token);

        String email = decodedJWT.getSubject();
        UserDetails userDetails = checkIfUserIsInDatabase(email);

        if(isAccessToken(decodedJWT.getClaim(TOKEN_TYPE_CLAIM_NAME)))
            validateAccessToken(decodedJWT, userDetails);
    }

    private void validateAccessToken(DecodedJWT decodedJWT, UserDetails userDetails) throws JWTVerificationException, UsernameNotFoundException {
        String email = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

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

    private boolean isAccessToken(Claim claim) {
        if (claim == null || claim.asString() == null) throw new JWTVerificationException("No token-type claim");
        else if (claim.asString().equals(TokenType.ACCESS_TOKEN.name().toLowerCase()))
            return true;
        else if(claim.asString().equals(TokenType.REFRESH_TOKEN.name().toLowerCase()))
            return false;
        else throw new JWTVerificationException("token-type claim not recognized");
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
    
    

    private enum TokenType {
        REFRESH_TOKEN, ACCESS_TOKEN
    }


}
