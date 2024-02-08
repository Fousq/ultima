package com.example.rbapp.config;

import com.example.rbapp.web.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static jakarta.servlet.DispatcherType.ERROR;
import static jakarta.servlet.DispatcherType.FORWARD;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain web(HttpSecurity httpSecurity, JwtAuthFilter jwtAuthFilter) throws Exception {
        return httpSecurity
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(registry ->
                        registry
                                .dispatcherTypeMatchers(FORWARD, ERROR).permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/course/subject/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/course/**").hasAnyRole("HEAD_TEACHER", "SUPERVISOR")
                                .requestMatchers(HttpMethod.PUT, "/api/course/**").hasAnyRole("HEAD_TEACHER", "TEACHER", "SUPERVISOR")
                                .requestMatchers("/api/user/**").permitAll()
                                .requestMatchers("/api/news/**").permitAll()
                                .requestMatchers("/api/book/**").permitAll()
                                .requestMatchers("/api/video/class/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/student").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/teacher").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/head/teacher").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/supervisor").permitAll()
                                .requestMatchers("/api/chat/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/student/send/application").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        ProviderManager providerManager = new ProviderManager(daoAuthenticationProvider);
        return providerManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
