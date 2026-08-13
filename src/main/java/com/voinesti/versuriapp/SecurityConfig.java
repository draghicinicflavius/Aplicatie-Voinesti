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
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((requests) -> requests
                // 1. Resurse publice
                .requestMatchers(
                    "/", 
                    "/live/**", 
                    "/song/**",
                    "/select-song/**", 
                    "/login", 
                    "/admin-login",
                    "/css/**", 
                    "/js/**", 
                    "/images/**", 
                    "/manifest.json", 
                    "/service-worker.js"
                ).permitAll()                

                // 2. Doar ADMIN (Flavius)
                .requestMatchers("/adauga", "/salveaza", "/edit/**", "/delete/**").hasRole("ADMIN")

                // 3. Orice altă cerere cerută
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/admin-login")
                .loginProcessingUrl("/admin-login") // Îi spunem exact unde să proceseze formularul
                .defaultSuccessUrl("/", true) // După login reuşit te duce pe pagina principală de unde poți edita/adăuga
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
        UserDetails flavius = User.builder()
            .username("flavius")
            .password(passwordEncoder().encode("214365qpR"))
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(flavius);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}