package sk.posam.fsa.nutritionplanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.nutritionplanner.domain.auth.service.AuthService;
import sk.posam.fsa.nutritionplanner.security.JwtService;

@Configuration
public class AuthBeanConfiguration {
    @Bean
    public AuthService authService() {
        return new AuthService();
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }
}
