package com.library.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    // ── Password Encoder ─────────────────────────────────────────
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ── Authentication Manager ───────────────────────────────────
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ── Security Filter Chain ────────────────────────────────────
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth

                // Public resources (CSS, JS, images)
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                // Login page — public
                .requestMatchers("/login").permitAll()

                // ADMIN-only actions (add, edit, delete)
                .requestMatchers("/authors/add", "/authors/edit/**", "/authors/delete/**").hasRole("ADMIN")
                .requestMatchers("/books/add",   "/books/edit/**",   "/books/delete/**"  ).hasRole("ADMIN")
                .requestMatchers("/members/add", "/members/edit/**", "/members/delete/**").hasRole("ADMIN")
                .requestMatchers("/borrow/new",  "/borrow/return/**"                     ).hasRole("ADMIN")

                // All other pages — any logged-in user
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")          // our custom login page
                .loginProcessingUrl("/login") // Spring handles POST here
                .defaultSuccessUrl("/", true) // redirect after login
                .failureUrl("/login?error")   // redirect on bad credentials
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")  // 403 page
            );

        return http.build();
    }
}
