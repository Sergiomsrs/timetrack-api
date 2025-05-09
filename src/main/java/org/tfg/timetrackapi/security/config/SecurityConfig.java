package org.tfg.timetrackapi.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.tfg.timetrackapi.security.jwt.JwtAuthenticationFilter;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas o accesibles sin autenticación
                        .requestMatchers("/api/auth/login", "/api/user/me", "/api/user/{id}", "/api/timestamp/fichar", "/api/timestamp/last3").permitAll()

                        // Rutas solo accesibles por administradores
                        .requestMatchers(HttpMethod.POST, "/api/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/user/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/user/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/user/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/user/pag").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/user/search").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/report/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/timestamp/add/{userId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/timestamp/employee/{employeeId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/timestamp/{timeTsId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/timestamp/timestamp").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/timestamp/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/timestamp/employee/{employeeId}/month").authenticated()
                        // Otras rutas de acceso restringido por rol ADMIN
                        .anyRequest().authenticated()  // Otras rutas requieren autenticación

                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Origen de tu frontend en desarrollo
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Preflight cache duration
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}

