package card.security.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import reactor.core.publisher.Mono;

@Service
public class authenticationFailureHandlerService implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
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
            
            DataBuffer buffer = null;
            
            try {
                buffer = response.bufferFactory().wrap(JSONObject.toJSONString(responseMap).getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.getStackTrace();
            }

            return response.writeWith(Mono.just(buffer));
    }
    
}
