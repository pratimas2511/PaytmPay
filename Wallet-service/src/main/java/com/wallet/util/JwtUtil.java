package com.wallet.util;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.expiration}")
	private long expiration;

    public String validateAndExtractUsername(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            return null; // Invalid or expired JWT
        }
    }

}
