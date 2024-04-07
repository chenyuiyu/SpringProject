package card.security.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;

import reactor.core.publisher.Mono;

@Service
public class serverAccessDeniedHandlerService implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("code", "denied");
        responseMap.put("msg", "账户无权限访问");

        DataBuffer buffer = null;
        ServerHttpResponse response = exchange.getResponse();
        try {
            buffer = response.bufferFactory().wrap(JSONObject.toJSONString(responseMap).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.getStackTrace();
        }

        return response.writeWith(Mono.just(buffer));
    }
    
}
