package com.library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;   // stored as BCrypt hash

    @Column(nullable = false)
    private String role;       // "ROLE_ADMIN" or "ROLE_USER"

    @Column(nullable = false)
    private boolean enabled = true;
}
