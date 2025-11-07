

package com.notflix.springVideo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.notflix.springVideo.services.UtenteService.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        //.csrf(csrf -> csrf.disable())
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(
                "/auth/admin/aggiornaUtenteAdmin", 
                 "/auth/register",
                 "/auth/admin/delete"  ) // disabilita CSRF solo per la tua chiamata PUT
        )
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/fonts/**").permitAll()
            .requestMatchers("/auth/login", "/auth/register", "/public/**").permitAll()
            .requestMatchers("/auth/admin/**").hasRole("ADMIN")
            .requestMatchers("/areaUtente/**").hasRole("USER")
            .requestMatchers(
                "/",
                "/catalogo/**",
                "/registi",
                "/attori",
                "/attore/**",
                "/scheda-film",
                "/scheda-serietv",
                "/film/**",
                "/serietv/**",
                "/modificaProfiloUtente",
                "/ricerca/**"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .exceptionHandling(ex -> ex.accessDeniedPage("/public/403"))
        .formLogin(form -> form
            .loginPage("/auth/login")
            .loginProcessingUrl("/auth/login")
            .defaultSuccessUrl("/", true)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/public/")
            .permitAll()
        );

    return http.build();
}

    @Bean
public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
}

@Autowired
private CustomUserDetailsService userDetailsService;

@Bean
public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder())
        .and()
        .build();
}


}

