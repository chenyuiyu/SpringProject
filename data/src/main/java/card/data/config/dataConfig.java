package card.data.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "data.resources")
@Component
public class dataConfig {
    public String sanguoxiuUrl;// 三国秀
    public String qunUrl;// 群
    public String shenUrl;// 神
    public String wuUrl;// 吴
    public String weiUrl;// 魏
    public String shuUrl;// 蜀

        // 类型
        public String monsterUrl;
        public String magicUrl;
        public String trapUrl;
        public String soulpendulumUrl;
    
        // 属性
        public String darkUrl;
        public String lightUrl;
        public String groundUrl;
        public String waterUrl;
        public String fireUrl;
        public String windUrl;
        public String godUrl;
        public String nodeUrl;
    
        // 效果
        public String originUrl;
        public String effectUrl;
        public String riteUrl;
        public String fusionUrl;
        public String homologyUrl;
        public String excessUrl;
        public String connectUrl;
        public String ramificationUrl;
}
