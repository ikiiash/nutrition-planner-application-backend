package sk.posam.fsa.nutritionplanner.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtDecoderConfiguration {

    private final JwtService jwtService;

    public JwtDecoderConfiguration(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(jwtService.getSigningKey()).build();
    }
}