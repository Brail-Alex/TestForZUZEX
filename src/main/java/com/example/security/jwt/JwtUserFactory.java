package com.example.security.jwt;

import com.example.model.User;

public class JwtUserFactory {
    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId()
        );
    }

}
