package card.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.security.Principal;
import card.data.UserRepository;
import card.domain.RegistrationForm;
import card.domain.User;
import card.domain.loginForm;
import card.domain.printForShow;
import card.security.service.JWT.JwtTokenUtils;
import reactor.core.publisher.Flux;
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
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;
    /**
     * 获取所有已注册的用户
     * @return
     */
    @GetMapping("/allUsers")
    @ResponseBody
    public Flux<User> getUser(){
        return userRepo.findByAuthority(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * 普通用户访问
     * post
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/user", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Mono<String> user(Principal principal) {

        return Mono.just("hello " + principal.getName());
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
    public Mono<String> admin(Principal principal,Authentication authentication) {
        return Mono.just("admin " + principal.getName());
    }

    
    @PostMapping(value = "/register", consumes = "application/json")
    @ResponseBody
    public Mono<Object> register(@RequestBody RegistrationForm form, ServerHttpResponse response) {
        Map<String, String> responseMap = new HashMap<>();

        if(userRepo.findByUsername(form.getUsername()).block() != null) {
            responseMap.put("code", "failure");
            responseMap.put("msg", "用户名已存在");
            response.setStatusCode(HttpStatus.CONFLICT);
        }
    
        responseMap.put("code", "success");
        responseMap.put("msg", "注册成功");
        response.setStatusCode(HttpStatus.CREATED);

        return userRepo.save(form.toUser(encoder)).map(user -> responseMap);
    }

    @PostMapping(value = "/login", consumes = "application/json")
    @ResponseBody
    public Mono<Object> login(@RequestBody loginForm form, ServerHttpResponse response) {
        Mono<User> user = userRepo.findByUsername(form.getUsername());
        Map<String, String> responseMap = new HashMap<>();
        User cur = user.block();
        if(cur == null || !cur.getPassword().equals(form.getPassword())) {
            responseMap.put("code", "failure");
            responseMap.put("msg", "用户不存在");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.just(responseMap);
        }
        return Mono.just(responseMap)
        .map(
            r -> {
                response.setStatusCode(HttpStatus.ACCEPTED);
                r.put("code", "success");
                r.put("msg", "登陆成功");
                r.put("token", JwtTokenUtils.createToken(cur.getUsername(), 86400));//token中包含了用户的用户名和是否为ADMIN
                return r;
            }
        );
    }
    

}