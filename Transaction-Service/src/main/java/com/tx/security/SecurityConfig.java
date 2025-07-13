package com.tx.security;

import com.tx.AuthenticationProvider.JWTAuthenticationProvider;
import com.tx.filter.JWTValidationFilter;
import com.tx.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private JwtUtil jwtUtil;

    SecurityConfig(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }


    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider() {
        return new JWTAuthenticationProvider(jwtUtil);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(
                jwtAuthenticationProvider()
        ));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        JWTValidationFilter jwtValidationFilter = new JWTValidationFilter(authenticationManager);

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll() // Allow login/signup
                        .anyRequest().authenticated() // All other APIs secured
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}


