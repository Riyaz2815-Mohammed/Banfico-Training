package com.riyaz.banficotrainingprogram.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.stream(allowedOrigins.split(",")).map(String::trim).filter(o -> !o.isEmpty()).toList());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(new GatewayAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/health", "/api/v1/info").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/v1/managers").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/managers").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/v2/customers").hasAnyRole("ADMIN", "BANKMANAGER")

                .requestMatchers(HttpMethod.POST, "/api/v1/accounts").hasAnyRole("ADMIN", "BANKMANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/accounts/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/accounts/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/v1/customers/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/customers/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/v1/beneficiaries/**").hasAnyRole("ADMIN", "BANKMANAGER", "USER")
                .requestMatchers(HttpMethod.POST, "/api/v1/beneficiaries/**").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/beneficiaries/**").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/beneficiaries/**").hasRole("USER")

                .requestMatchers("/api/v2/transfer", "/api/v2/transfer/preview").hasRole("USER")

                .requestMatchers("/api/v1/profile/**").hasRole("USER")

                .requestMatchers(HttpMethod.GET, "/api/v1/payments/**").hasAnyRole("ADMIN", "BANKMANAGER", "USER")

                .anyRequest().authenticated()
            );
        return http.build();
    }
}
