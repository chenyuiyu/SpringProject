package card.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.security.Principal;
import card.data.UserRepository;
import reactor.core.publisher.Mono;

//注意：@EnableWebFlux用来配置freemarker，它适用于模板文件，但是对静态资源访问有问题，访问纯静态资源请注释
@EnableWebFlux
@RestController
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class RegistrationController{

    /**
     此示例，只提供api级别的security接口较验
     测试流程：
     1.通过postamn 以get访问：http://127.0.0.1:8080/getUser 未登录，被拦截，跳转到/login(get模式)，返回未登录提示
     2.通过postamn 以post模式访问: http://127.0.0.1:8080/login，参数通过body （x-wwww-from-urlencoded)表单提交
     2.1. 登录成功，进入ServerAuthenticationSuccessHandler类中加工返回数据
     2.2. 登录失败，进入ServerAuthenticationFailureHandler类中加工返回数据
     2.3. 登录成功后，继续通过postamn 以get访问：http://127.0.0.1:8080/getUser，响应正常
     */

    /**
     * 不限制用户访问（需登录）
     * @return
     */
    @RequestMapping("/getUser")
    @ResponseBody
    public Mono<String> getUser(){
        return Mono.just("getUser");
    }

    /**
     * 普通用户访问
     * post
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/user", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Mono<String> user(Principal principal){
        System.out.println(principal.getName());
        return Mono.just("hello "+principal.getName());
    }

    /**
     * 超管用户访问
     * @return
     */
//    @PreAuthorize("hasPermission('ROLE_ADMIN')")
//    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/admin", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Mono<String> admin(Principal principal,Authentication authentication){
        System.out.println(authentication.getAuthorities());
        System.out.println(principal.getName());
        return Mono.just("admin "+principal.getName());
    }

    @RequestMapping(value = "/login", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Mono<Object> login(ServerHttpResponse response){
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("code", "failure");
        responseMap.put("msg", "您还未登录");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return Mono.just(responseMap);
    }

}