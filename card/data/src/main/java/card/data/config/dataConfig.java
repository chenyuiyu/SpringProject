package card.data.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "data.resources")
@Component
public class dataConfig {
    public String sanguoxiuUrl;//三国秀
    public String qunUrl;//群
    public String shenUrl;//神
    public String wuUrl;//吴
    public String weiUrl;//魏
    public String shuUrl;//蜀
}
