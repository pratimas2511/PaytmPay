package com.tx.AuthenticationProvider;

import com.tx.token.JWTAuthenticationToken;
import com.tx.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

public class JWTAuthenticationProvider implements AuthenticationProvider {

    private JwtUtil jwtUtil;

    public JWTAuthenticationProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = ((JWTAuthenticationToken) authentication).getToken();

        String username = jwtUtil.validateAndExtractUsername(token);
        if (username == null) {
            throw new BadCredentialsException("Invalid JWT Token");
        }

        return new JWTAuthenticationToken(username, token, List.of());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
