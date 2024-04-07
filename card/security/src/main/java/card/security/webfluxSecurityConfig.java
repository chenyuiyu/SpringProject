package card.security;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;

import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
public class webfluxSecurityConfig {

    @Autowired
    private ReactiveUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        log.info("加载security 权限配置....");
        return http
        .csrf().disable()
        .cors().disable()
        .httpBasic().disable()
        .formLogin()
        .authenticationFailureHandler((webFilterExchange, exception) -> { //验证失败处理器(可以单独创建类处理)
            webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("code", "failure");
            if (exception instanceof UsernameNotFoundException) {
                responseMap.put("msg", "用户不存在 " + exception.getMessage());
            } else if (exception instanceof BadCredentialsException) {
                responseMap.put("msg", "密码错误 " + exception.getMessage());
            } else if (exception instanceof LockedException) {
                responseMap.put("msg", "用户锁定 " + exception.getMessage());
            } else if (exception instanceof AccountExpiredException) {
                responseMap.put("msg", "账户过期 " + exception.getMessage());
            } else if (exception instanceof DisabledException) {
                responseMap.put("msg", "账户不可用 " + exception.getMessage());
            } else {
                responseMap.put("msg", "系统错误 " + exception.getMessage());
            }
            //responseMap.put("msg", exception.getMessage());
            return writeWith(webFilterExchange.getExchange(), responseMap);
        })
        .loginPage("/login")
        .and()
        .authorizeExchange()
        //.pathMatchers(HttpMethod.OPTIONS).permitAll()//特殊请求过滤
        .pathMatchers("/login").permitAll()//登录不需要验证
        .anyExchange().permitAll()
        .and().logout()
                .logoutSuccessHandler((webFilterExchange, authentication) -> { //退出成功处理器(可以单独创建类处理)
                    Map<String, String> responseMap = new HashMap<>();
                    responseMap.put("code", "logout");
                    responseMap.put("msg", "退出成功");
                    return writeWith(webFilterExchange.getExchange(), responseMap);
                }).and()
                .exceptionHandling()
                .accessDeniedHandler((exchange, denied) -> { // 无权限访问处理器(可以单独创建类处理)
                    Map<String, String> responseMap = new HashMap<>();
                    responseMap.put("code", "denied");
                    responseMap.put("msg", "账户无权限访问");
                    return writeWith(exchange, responseMap);
                })
        .and()
        .authenticationManager(this.authenticationManager())
        .build();
    }

    @Bean
    ReactiveAuthenticationManager authenticationManager() {
        log.info("加载security 用户配置....");
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(encoder);
        return authenticationManager;
    }

    public Mono<Void> writeWith(ServerWebExchange exchange, Map<String, String> responseMap){
        ServerHttpResponse response = exchange.getResponse();
        String body = JSONObject.toJSONString(responseMap);
        DataBuffer buffer = null;
        try{
            buffer = response.bufferFactory().wrap(body.getBytes("UTF-8"));
        }catch(UnsupportedEncodingException ue){
            ue.printStackTrace();
        }
        return response.writeWith(Mono.just(buffer));
    }
    
}


/* 
@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class webfluxSecurityConfig {
  
  @Autowired
  private UserDetailsService userDetailsService;
  
  @Autowired
  private PasswordEncoder encoder;

  @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 用于处理 Angular/CORS
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated()
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
        .csrf()
            .ignoringAntMatchers("/h2-console/**", "/api/**")
        .and()
        .headers()
            .frameOptions()
                .sameOrigin();

        return http.build();
    }
  
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }

}
*/

