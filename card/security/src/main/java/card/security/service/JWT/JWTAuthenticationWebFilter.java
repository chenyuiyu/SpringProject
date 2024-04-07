package card.security.service.JWT;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import card.data.UserRepository;
import card.domain.User;
import reactor.core.publisher.Mono;

@Service
public class JWTAuthenticationWebFilter implements WebFilter{

    @Autowired
    private UserRepository userRepo;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Map<String, String> responseMap = new HashMap<>();
        String path = exchange.getRequest().getPath().value();
        if(path.startsWith("/api/")) {
            //拦截所有的api路径
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if(StringUtils.isNotBlank(token)) {
                String username = null;
                try {
                    username = JwtTokenUtils.parseToken(token);
                } catch (TokenExpiredException e1) {
                    //token过时
                    return authenticationFailure(responseMap, "过时的token", exchange);
                } catch (SignatureVerificationException e2) {
                    //无效的token
                    return authenticationFailure(responseMap, "无效的token", exchange);
                }
                Mono<User> userDetails = userRepo.findByUsername(username);
                User user = userDetails.block();
                if (user == null){
                    return authenticationFailure(responseMap, "未知的用户", exchange);
                }
                Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(auth);
                SecurityContextHolder.setContext(securityContext);//将这个安全上下文绑定到当前线程供后序验证使用

            } else return authenticationFailure(responseMap, "空白的token", exchange);
            
        } 
        return chain.filter(exchange);
    }

    private Mono<Void> wirteWithMap(Map<String, String> records, ServerHttpResponse response) {
        DataBuffer buffer = null;
        try {
            buffer = response.bufferFactory().wrap(JSONObject.toJSONString(records).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response.writeWith(Mono.just(buffer));
    }

    private Mono<Void> authenticationFailure(Map<String, String> responseMap, String msg, ServerWebExchange exchange) {
        responseMap.put("code", "failure");
        responseMap.put("msg", msg);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
    
}
