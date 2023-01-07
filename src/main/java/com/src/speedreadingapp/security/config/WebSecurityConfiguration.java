package com.src.speedreadingapp.security.config;


import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.security.config.filters.CustomAuthenticationFilter;
import com.src.speedreadingapp.security.config.filters.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean(), appUserService));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(
                "/api/login","/api/v1/registration", "/api/v1/user/test2", "/api/v1/session/**", "/api/v1/user-progress/**", "/api/v1/understanding-meter/**").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/registration/confirm").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/speed-meter-log/save").permitAll();
        http.authorizeRequests()
                .antMatchers("/api/v1/schultz-array-logs/*/{userId}")
                .access("@userEndpointSecurity.hasUserId(authentication,#userId)");
        http.authorizeRequests().antMatchers("/api/v1/pdfuser/save/*").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/pdfuser/get/*").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/esp-storage/save").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/esp-storage/get/*").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/pdfuser/get-text/*").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/speed-meter-log/get/{userId}").access("@userEndpointSecurity.hasUserId(authentication,#userId)");
        http.authorizeRequests().antMatchers(
                "/api/v1/speed-meter-log/get-latest/{userId}",
                            "/api/v1/column-numbers-logs/*/{userId}",
                            "/api/v1/numbers-disappear-logs/*/{userId}"
                        ).access("@userEndpointSecurity.hasUserId(authentication,#userId)");
        //http.authorizeRequests().anyRequest().authenticated();//and().formLogin();
        http.authorizeRequests().anyRequest().authenticated();//and().formLogin();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of( "http://localhost:3000", "http://192.168.1.10:3000",  "https://speed-reading-app-frontend.herokuapp.com", "speedreadingapp.loca.lt", "http://speedreadingapplication.eu.loclx.io", "http://speedreadingapplication.pl"));
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Access-Control-Allow-Origin", "Access-Control-Request-Method", "Authorization", "Cache-Control", "Content-Type", "*"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder.bCryptPasswordEncoder());
        provider.setUserDetailsService(appUserService);
        return provider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
