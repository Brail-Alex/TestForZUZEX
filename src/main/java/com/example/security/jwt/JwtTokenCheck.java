package com.example.security.jwt;

import com.example.exception.JwtAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtTokenCheck {
    private JwtToken jwtToken;
    @Autowired
    public JwtTokenCheck(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }
    public String checkToken(String token) throws JwtAuthenticationException {
        String username = "";
        String tokenResolved = jwtToken.resolveToken(token);
        try {
            if (jwtToken.validateToken(tokenResolved)) {
                username = jwtToken.getUsername(tokenResolved);
            }
            return username;
        } catch (JwtAuthenticationException e) {
            throw new JwtAuthenticationException("Token invalid!");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
