package com.wallet.AuthenticationProvider;

import com.wallet.token.JWTAuthenticationToken;
import com.wallet.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
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
