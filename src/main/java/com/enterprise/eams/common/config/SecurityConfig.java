package com.enterprise.eams.common.config;

import com.enterprise.eams.common.security.filter.JwtAuthenticationFilter;
import com.enterprise.eams.common.security.handler.JwtAccessDeniedHandler;
import com.enterprise.eams.common.security.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                jwtAuthenticationEntryPoint
                        )

                        .accessDeniedHandler(
                                jwtAccessDeniedHandler
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC AUTH APIs
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // USER MANAGEMENT -> MANAGER ONLY
                        .requestMatchers("/api/users/**")
                        .hasRole("MANAGER")

                        // REPORTS -> MANAGER ONLY
                        .requestMatchers("/api/reports/**")
                        .hasRole("MANAGER")

                        // ASSET CREATE -> MANAGER
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/assets/**"
                        ).hasRole("MANAGER")

                        // ASSET UPDATE -> MANAGER
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/assets/**"
                        ).hasRole("MANAGER")

                        // ASSET DELETE -> MANAGER
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/assets/**"
                        ).hasRole("MANAGER")

                        // ASSET VIEW -> BOTH
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/assets/**"
                        ).hasAnyRole("MANAGER", "OPERATOR")

                        // SENSOR APIs -> BOTH
                        .requestMatchers("/api/sensors/**")
                        .hasAnyRole("MANAGER", "OPERATOR")

                        // ALERT APIs -> BOTH
                        .requestMatchers("/api/alerts/**")
                        .hasAnyRole("MANAGER", "OPERATOR")

                        // MAINTENANCE APIs -> BOTH
                        .requestMatchers("/api/maintenance/**")
                        .hasAnyRole("MANAGER", "OPERATOR")

                        // DOWNTIME APIs -> BOTH
                        .requestMatchers("/api/downtime/**")
                        .hasAnyRole("MANAGER", "OPERATOR")

                        // EVERYTHING ELSE REQUIRES LOGIN
                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}