package card.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@SuppressWarnings("deprecation")
public class securityConfig  {

    @Autowired
    private UserDetailsService userDetailsService;

    @SuppressWarnings("removal")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers("/login").permitAll() // 允许登录接口无需认证
                .requestMatchers("/api/**").access("hasRole('ROLE_USER')")
                .requestMatchers("/**").permitAll() // 允许其他请求
            .and()
                .formLogin()
                    .loginPage("/login")
            .and()
                .httpBasic()
                    .realmName("Card Design")
            .and()
                .logout()
                    .logoutSuccessUrl("/")
            .and()
                .csrf().ignoringRequestMatchers("/h2-console/**", "/api/**")
            .and()
                .headers()
                    .frameOptions()
                    .sameOrigin();
        return http.build();
    }

    @Bean
    PasswordEncoder encoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    
    
    @Bean
    AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(null);
        builder.userDetailsService(userDetailsService).passwordEncoder(encoder());
        return builder.build();
    }
}
