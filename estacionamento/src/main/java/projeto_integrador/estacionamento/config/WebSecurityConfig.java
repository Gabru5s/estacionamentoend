package projeto_integrador.estacionamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF – se seu front é SPA/REST, normalmente desativa geral:
                .csrf(csrf -> csrf.disable())

                // H2 console usa frame, então precisa liberar
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                )

                .authorizeHttpRequests(auth -> auth
                        // ENDPOINTS PÚBLICOS (SEM AUTENTICAÇÃO)
                        .requestMatchers(
                                "/api/usuarios/cadastro",
                                "/api/usuarios/login",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/h2-console/**",
                                "/api/vagas/**",
                                "/api/reservas/whatsapp/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
