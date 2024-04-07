package card.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

public class JwtSecurityContextRepository implements ServerSecurityContextRepository {
    private UserRepositoryUserDetailsService userService;

    public JwtSecurityContextRepository(UserRepositoryUserDetailsService userService){
        this.userService = userService;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        //log.info("加载token:JwtSecurityContextRepository");
        String path = exchange.getRequest().getPath().toString();
        // 过滤路径
        if ("/login".equals(path)){
            return Mono.empty();
        }
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(token)){
            // token能正常解析，表示token有效并对应数据库已知用户
            String subject = JwtTokenUtils.parseToken(token);
            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(subject, subject);
            return new ReactiveAuthenticationManager(){
                @Override
                public Mono<Authentication> authenticate(Authentication authentication) {
                    // 如果对token有足够的安全认可，可以采用无状态凭证策略，将username和authorities放置在token串中解析获取，此处就可以不用查询数据库验证
                    Mono<UserDetails> userDetails = userService.findByUsername(authentication.getPrincipal().toString());
                    UserDetails user = userDetails.block();
                    if (user == null){
                        throw new DisabledException("账户不可用");
                    }
                    Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
                    return Mono.just(auth);
                }
            }.authenticate(newAuthentication).map(SecurityContextImpl::new);
        }else {
            return Mono.empty();
        }
    }
}
