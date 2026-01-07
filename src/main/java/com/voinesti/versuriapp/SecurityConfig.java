package com.voinesti.versuriapp; // Asigură-te că pachetul corespunde cu cel din proiectul tău

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // 1. Oricine poate vedea pagina de login și resursele statice (CSS/Imagini)
                .requestMatchers("/login", "/css/**", "/images/**").permitAll()
                
                // 2. Doar ADMIN-ul (tu) are voie să adauge sau să modifice ceva
                .requestMatchers("/admin/**", "/adauga", "/editare/**", "/schimba-piesa").hasRole("ADMIN")
                
                // 3. Orice altă pagină (vizualizarea versurilor) necesită logare (fie USER, fie ADMIN)
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login") // Vom crea noi o pagină de login frumoasă mai târziu
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout((logout) -> logout.permitAll());

        return http.build();
    }
}