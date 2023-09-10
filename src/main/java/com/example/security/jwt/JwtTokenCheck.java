package com.example.security.jwt;

import com.example.exception.JwtAuthenticationException;
import com.example.security.jwt.JwtToken;

public class JwtTokenCheck {
    private final JwtToken jwtToken;

    public JwtTokenCheck(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String checkToken(String toKen) throws JwtAuthenticationException {
        String username = "";
        String tokenResolved = jwtToken.resolveToken(toKen);
     try {
         if (jwtToken.validateToken(tokenResolved)) {
             username = jwtToken.getUserData(tokenResolved);
         }
         return username;
     } catch (JwtAuthenticationException e) {
             throw new JwtAuthenticationException("Token invalid!");

     } catch (Exception e) {
         throw new RuntimeException(e);
     }
 }
}
