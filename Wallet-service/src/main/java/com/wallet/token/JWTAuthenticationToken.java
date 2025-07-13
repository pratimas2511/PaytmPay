package com.wallet.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    private final Object principal;

    // Constructor for the unauthenticated token (before validation)
    public JWTAuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.principal = null;
        setAuthenticated(false);
    }

    // Constructor for the authenticated token (after successful validation)
    public JWTAuthenticationToken(Object principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.principal = principal;
        setAuthenticated(true);
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}