package card.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebFluxSecurity
public class securityConfig  {

    @Autowired
    private ReactiveUserDetailsService userDetailsService;

    @SuppressWarnings("removal")
    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
        .authorizeExchange(exchange ->exchange
            //.pathMatchers(HttpMethod.OPTIONS).permitAll()
            //.pathMatchers("/login").permitAll() // 允许登录接口无需认证
            //.pathMatchers("/api/**").hasRole("USER")
            .anyExchange().permitAll())
            .authenticationManager(this.authenticationManager());
        return http.build();
    }

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
     
    @Bean
    ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(this.encoder());
        return authenticationManager;
    }
}
