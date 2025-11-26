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
                // Desabilita CSRF (API REST)
                .csrf(csrf -> csrf.disable())

                // Habilita CORS (vai usar seu CorsConfig se tiver)
                .cors(cors -> {})

                // Libera uso de frame (necessário pro H2 console)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                )

                // Libera TUDO sem precisar de autenticação
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
