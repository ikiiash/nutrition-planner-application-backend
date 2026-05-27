package sk.posam.fsa.nutritionplanner.security;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    private final RestSecurityExceptionHandler restSecurityExceptionHandler;

    public SecurityConfiguration(RestSecurityExceptionHandler restSecurityExceptionHandler) {
        this.restSecurityExceptionHandler = restSecurityExceptionHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/health", "/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/food-products/**").hasAnyRole("ADMIN", "USER", "PREMIUM_USER")
                        .requestMatchers(HttpMethod.POST, "/food-products/**").hasAnyRole("ADMIN", "USER", "PREMIUM_USER")
                        .requestMatchers(HttpMethod.PUT, "/food-products/**").hasAnyRole("ADMIN", "USER", "PREMIUM_USER")
                        .requestMatchers(HttpMethod.DELETE, "/food-products/**").hasAnyRole("ADMIN", "USER", "PREMIUM_USER")
                        .requestMatchers(HttpMethod.PATCH, "/food-products/**").hasAnyRole("ADMIN", "USER", "PREMIUM_USER")
                        .requestMatchers("/shopping-list/**").hasAnyRole("ADMIN", "USER", "PREMIUM_USER")
                        .requestMatchers(HttpMethod.GET, "/user-profile/**").hasAnyRole("ADMIN", "USER", "PREMIUM_USER")
                        .requestMatchers(HttpMethod.PUT, "/user-profile/**").hasAnyRole("ADMIN", "USER", "PREMIUM_USER")
                        .requestMatchers(HttpMethod.POST, "/ai/chat").hasAnyRole("ADMIN", "PREMIUM_USER")
                        .requestMatchers(HttpMethod.POST, "/ai/autofill").hasAnyRole("ADMIN", "PREMIUM_USER")
                        .requestMatchers("/ai/chats/**").hasAnyRole("ADMIN", "PREMIUM_USER")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(restSecurityExceptionHandler)
                        .accessDeniedHandler(restSecurityExceptionHandler))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {
                            jwt.decoder(jwtDecoder);
                            jwt.jwtAuthenticationConverter(new JwtAuthenticationConverter());
                        })
                );

        return http.build();
    }
}
