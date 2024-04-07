package card.security;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;

import card.security.service.authenticationFailureHandlerService;
import card.security.service.logoutSuccessHandlerService;
import card.security.service.serverAccessDeniedHandlerService;
import card.security.service.JWT.JWTAuthenticationWebFilter;
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

    @Autowired
    private authenticationFailureHandlerService authenticationFailureHandler;

    @Autowired
    private logoutSuccessHandlerService logoutSuccessHandler;

    @Autowired
    private serverAccessDeniedHandlerService serverAccessDeniedHandler;

    @SuppressWarnings("removal")
    @Profile("!prod")
    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JWTAuthenticationWebFilter JWTfilter) {
        log.info("加载security 权限配置....");
        return http
        .csrf().disable()
        .cors().disable()
        .httpBasic().disable()//注释这个可以正常设置rest基础路径
        .authenticationManager(this.authenticationManager())//自定义用户和加密
        .addFilterAt(JWTfilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .authorizeExchange()
        //.pathMatchers(HttpMethod.OPTIONS).permitAll()//特殊请求过滤
        .pathMatchers("/login").permitAll()//登录不需要验证
        .pathMatchers("/api/**").authenticated()
        .anyExchange().permitAll()
        .and()
        .formLogin()
        .loginPage("/login")
        .authenticationFailureHandler(authenticationFailureHandler)//认证失败处理器
        .and().logout()
        .logoutSuccessHandler(logoutSuccessHandler).and()//登出成功处理器
        .exceptionHandling()
        .accessDeniedHandler(serverAccessDeniedHandler)//访问拒绝处理器
        .and()
        .build();
    }

    @Bean
    ReactiveAuthenticationManager authenticationManager() {
        log.info("加载security 用户配置....");
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(encoder);
        return authenticationManager;
    }

    //辅助函数，用于将JSON数据写入response
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

