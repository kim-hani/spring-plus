package org.example.expert.domain.common.dto;

import lombok.Getter;
import org.example.expert.domain.user.enums.UserRole;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;

    private final String username;
    private final UserRole userRole;

    public AuthUser(Long id, String email,String username,UserRole userRole) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.userRole = userRole;
    }
}
