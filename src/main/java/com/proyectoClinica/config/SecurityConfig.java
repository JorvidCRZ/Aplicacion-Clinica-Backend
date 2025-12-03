package com.proyectoClinica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.config.Customizer;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Habilita CORS (usa el CorsConfigurationSource declarado más abajo)
                .cors(Customizer.withDefaults())
                // Deshabilita CSRF (importante para APIs REST)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/pacientes/**").permitAll()
                        .requestMatchers("/usuarios/**").permitAll()
                        .requestMatchers("/medicos/**").permitAll() // ✅ agregado
                        .requestMatchers(HttpMethod.GET, "/especialidades/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/subespecialidades/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/contactos").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/reportes/**").permitAll()
                        // El resto también se permite por ahora (para desarrollo)
                        .anyRequest().permitAll()
                )
                // Autenticación básica (solo si la necesitas)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // Configuración global de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:4200")); // origen del front (Angular)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // si no usas sesión, puedes poner false

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
