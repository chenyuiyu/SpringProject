package card.security.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import reactor.core.publisher.Mono;

@Service
public class logoutSuccessHandlerService implements ServerLogoutSuccessHandler {

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("code", "logout");
        responseMap.put("msg", "退出成功");

        DataBuffer buffer = null;
        ServerHttpResponse response = exchange.getExchange().getResponse();
        try {
            buffer = response.bufferFactory().wrap(JSONObject.toJSONString(responseMap).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.getStackTrace();
        }

        return response.writeWith(Mono.just(buffer));
    }
    
}
