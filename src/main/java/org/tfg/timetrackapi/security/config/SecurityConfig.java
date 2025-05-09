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
                        // Rutas Libres
                        .requestMatchers("/api/auth/login", "/api/timestamp/fichar").permitAll()

                        // Rutas Autenticadas
                        .requestMatchers("/api/user/me").authenticated()

                        // Rutas Solo Rol ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/user/{id}").hasRole("ADMIN")   // Permitir GET
                        .requestMatchers(HttpMethod.PUT, "/api/user/{id}").hasRole("ADMIN")   // Permitir PUT
                        .requestMatchers(HttpMethod.DELETE, "/api/user/{id}").hasRole("ADMIN")  // Permitir DELETE
                        .requestMatchers(HttpMethod.PATCH, "/api/timestamp/{recordId}").hasRole("ADMIN")  // Permitir PATCH
                        .requestMatchers(HttpMethod.GET, "/api/timestamp/employee/*/month").hasRole("ADMIN")

                        .requestMatchers("/api/report/employee/{employeeId}/report/pdf/monthly", "/api/timestamp/timestamp","api/employee/{employeeId}/month", "/api/user/{editedEmployeeId}", "/api/user/{employeeId}")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/user/search").hasRole("ADMIN")
                        .requestMatchers("/api/user/pag").hasRole("ADMIN")

                        // Rutas rol admin y guest
                        .requestMatchers("/api/user").hasAnyRole("ADMIN", "GUEST")
                        .anyRequest().authenticated()
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

