package com.voinesti.versuriapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // 1. Resurse libere
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/manifest.json", "/service-worker.js").permitAll()                
                // 2. Doar ADMIN (Flavius) poate adăuga/șterge/edita
                .requestMatchers("/adauga", "/salveaza", "/edit/**", "/delete/**").hasRole("ADMIN")
                
                // 3. Admin și Dirijor pot selecta piesa pentru cor
                .requestMatchers("/select-song/**").hasAnyRole("ADMIN", "DIRIJOR")
                
                // 4. Toți cei logați pot vedea pagina de live și lista
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                // LOGICA DE REDIRECȚIONARE AUTOMATĂ
                .successHandler((request, response, authentication) -> {
                    var authorities = authentication.getAuthorities();
                    // Dacă e simplu UTILIZATOR, trimite-l direct la /live
                    if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
                        response.sendRedirect("/live");
                    } else {
                        // Adminul și Dirijorul merg la lista de piese
                        response.sendRedirect("/");
                    }
                })
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // 1. ADMIN (Flavius) - Control total
        UserDetails flavius = User.builder()
            .username("flavius")
            .password(passwordEncoder().encode("1234"))
            .roles("ADMIN")
            .build();

        // 2. DIRIJOR - Doar alege piesele
        UserDetails dirijor = User.builder()
            .username("dirijor")
            .password(passwordEncoder().encode("1234"))
            .roles("DIRIJOR")
            .build();

        // 3. UTILIZATOR - Vede doar pagina live
        UserDetails utilizator = User.builder()
            .username("utilizator")
            .password(passwordEncoder().encode("1234"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(flavius, dirijor, utilizator);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}