package RedSource.Config.security;

import RedSource.utils.JwtTokenGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class JwtConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenGenerator jwtTokenGenerator, UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtTokenGenerator, userDetailsService);
    }
}
