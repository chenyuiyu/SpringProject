package card.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtils {
    /**
     JSON Web Token（缩写 JWT）是目前最流行的跨域认证解决方案。
     JWT 的三个部分依次如下
     Header（头部）:是一个 JSON 对象，描述 JWT 的元数据{ "alg": "HS256", typ": "JWT" }
     Payload（负载）:也是一个 JSON 对象，用来存放实际需要传递的数据,JWT 规定了7个官方字段:
         iss (issuer)：签发人
         exp (expiration time)：过期时间
         sub (subject)：主题
         aud (audience)：受众
         nbf (Not Before)：生效时间
         iat (Issued At)：签发时间
         jti (JWT ID)：编号
     Signature（签名）:对前两部分的签名，防止数据篡改

     1.JWT中Header头和Payload有效载荷序列化的算法都用到了Base64URL，签名哈希部分是对Header与Payload两部分数据签名
     2.客户端接收服务器返回的JWT，将其存储在Cookie或localStorage中，客户端将在与服务器交互中都会带JWT，将它放入HTTP请求的Header Authorization字段中
     3.JWT的最大缺点是服务器不保存会话状态，所以在使用期间不可能取消令牌或更改令牌的权限
     4.JWT本身包含认证信息，因此一旦信息泄露，任何人都可以获得令牌的所有权限
     5.JWT不建议使用HTTP协议来传输代码，而是使用加密的HTTPS协议进行传输
     */

    /**
     * 加密密钥
     */
    private static final String SECRET = "f2f4f94c9065_wNmx01w27MQnPc3BtUQkty_23P0pVlAdj86o5XznUrE";

    /**
     * jwt创建token，考虑安全性，token中不因该放入太多信息（勿放密码之类的敏感信息），只放入关键字段值即可，如用户ID
     * @param sub     主题（可以放入关键数据，如:userid, 用户唯一值等）
     * @param timeout 过期时长（秒）
     * @return
     */
    public static String createToken(String sub, int timeout) {
        JWTCreator.Builder builder = JWT.create();
        builder.withSubject(sub);//主题
        builder.withIssuer("pro-server");
        if (timeout>0) {
            builder.withExpiresAt(DateUtils.addSeconds(new Date(), timeout));//过期时间
        }
        return builder.sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 对jwt创建的token进行验签与解析，返回Subject（主题）中存放的内容
     * @param token
     * @return
     * @throws TokenExpiredException          会话超时异常
     * @throws SignatureVerificationException 验签无效异常
     */
    public static String parseToken(String token) throws TokenExpiredException, SignatureVerificationException {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token).getSubject();
    }

    /**
     * jwt创建token，考虑安全性，token中不因该放入太多信息（勿放密码之类的敏感信息）
     * @param loadMap   数据集合
     * @param timeout   过期时长（秒）
     * @return
     */
    public static String createToken(Map<String, Object> loadMap, int timeout) {
        JWTCreator.Builder builder = JWT.create();
        loadMap.forEach((k, v) -> {
            if (v instanceof String) {
                builder.withClaim(k, (String) v);
            } else if (v instanceof Date) {
                builder.withClaim(k, (Date) v);
            } else if (v instanceof Long) {
                builder.withClaim(k, (Long) v);
            } else if (v instanceof Integer) {
                builder.withClaim(k, (Integer) v);
            } else if (v instanceof Boolean) {
                builder.withClaim(k, (Boolean) v);
            }
        });
        builder.withIssuer("pro-server");
        if (timeout>0) {
            builder.withExpiresAt(DateUtils.addSeconds(new Date(), timeout));//过期时间
        }
        return builder.sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 对jwt创建的token进行验签与解析，返回集合
     * @param token
     * @return
     * @throws TokenExpiredException          会话超时异常
     * @throws SignatureVerificationException 验签无效异常
     */
    public static Map<String, Object> parseTokenToMap(String token) throws TokenExpiredException, SignatureVerificationException {
        Map<String, Claim> claimMap = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token).getClaims();
        if (claimMap == null){
            return null;
        }
        Map<String, Object> loadMap = new HashMap<>();
        claimMap.forEach((k, v) -> {
            Object obj = null;
            if (v.asString() != null) {
                obj = v.asString();
            } else if (v.asBoolean() != null) {
                obj = v.asBoolean();
            } else if (v.asDate() != null || v.asLong() != null) {//Date类型按Long方式来处理
                obj = v.asLong();
            } else if (v.asInt() != null) {
                obj = v.asInt();
            }
            loadMap.put(k, obj);
        });
        return loadMap;
    }
}
