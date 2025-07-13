package com.auth.securityConfig;

import com.auth.AuthenticationProviders.JWTAuthenticationProvider;
import com.auth.filter.JWTAuthenticationFilter;
import com.auth.filter.JWTValidationFilter;
import com.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {

    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;

    @Autowired
    public AuthConfiguration(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider() {
        return new JWTAuthenticationProvider(jwtUtil, userDetailsService);
    }

     @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager,
                                                   JwtUtil jwtUtil) throws Exception {

        // Authentication filter responsible for login
        JWTAuthenticationFilter jwtAuthFilter = new JWTAuthenticationFilter(authenticationManager, jwtUtil);

        // Validation filter for checking JWT in every request
        JWTValidationFilter jwtValidationFilter = new JWTValidationFilter(authenticationManager);


        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register","/auth/generate-token").permitAll()
                        .requestMatchers("/auth/users").hasRole("USER")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // generate token filter
                .addFilterAfter(jwtValidationFilter, JWTAuthenticationFilter.class); // validate token filter

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(
                daoAuthenticationProvider(),
                jwtAuthenticationProvider()
        ));
    }
}


