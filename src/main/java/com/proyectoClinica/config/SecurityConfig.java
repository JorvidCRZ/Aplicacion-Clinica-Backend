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
        // Para API REST suele deshabilitarse CSRF (si usas cookies/session, revísalo)
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/usuarios/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/especialidades/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/subespecialidades/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/contactos").permitAll()
                        // .requestMatchers("/contactos", "/contactos/**").permitAll()
                        // permitir preflight OPTIONS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated())

                // Mantén httpBasic solo si lo necesitas; en producción probablemente usarás
                // otro mecanismo
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // Bean CORS global — ajusta allowedOrigins para producción.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // En desarrollo incluye los orígenes desde los que servirás el front.
        // Añade 127.0.0.1 si arrancas el front con esa IP, o el dominio de producción
        // cuando lo despliegues.
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // si usas cookies/sesión; si no, poner false

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

