package com.example.progettopsw.configurations.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for handling HTTP requests, OAuth2 login, and logout.
     *
     * @param http HttpSecurity object to define web-based security at the HTTP level
     * @return SecurityFilterChain for filtering and securing HTTP requests
     * @throws Exception in case of an error during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) /// Disabilita CSRF per il testing
                // Configures authorization rules for different endpoints
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll() // Allows public access to the root URL
                        .requestMatchers("/home").authenticated() // Requires authentication to access "/menu"
                        .anyRequest().authenticated() // Requires authentication for any other request
                )
                // Configures OAuth2 login settings
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/keycloak") // Sets custom login page for OAuth2 with Keycloak
                        .defaultSuccessUrl("/home", true) // Redirects to "/menu" after successful login
                )
                // Configures logout settings
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // Redirects to the root URL on successful logout
                        .invalidateHttpSession(true) // Invalidates session to clear session data
                        .clearAuthentication(true) // Clears authentication details
                        .deleteCookies("JSESSIONID") // Deletes the session cookie
                );

        return http.build();
    }
}
