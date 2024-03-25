package card.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Document
public class yugiohCard {
    
    @Id
    private String id;//主键

    private String name;//名字

    private boolean gradientColor = false;//是否采用渐变色

    private CardCatalog cardCatalog;//卡片大分类

    private ElementType elementType;//元素类型
    
    private String header;//描述，用于效果栏上方，例：[龙族/通常]
    private String description;//效果描述

    private innerPrint print;//图片

    @Max(value = 13, message = "星级最高为13")
    private int stars;//星级

    //对于攻击力和防御力,-1为?,-2为无穷
    @Min(value = -2, message = "-1表示?,-2表示无穷大,不存在其他的负数攻击力")
    private int atk;//攻击力

    @Min(value = -2, message = "-1表示?,-2表示无穷大,不存在其他的负数防御力")
    private int def;//防御力

    public enum CardCatalog {
        MONSTER, MAGIC, TRAP, SOULPENDULUM
    }//怪兽、魔法、陷阱、灵摆

    public enum ElementType {
        DARK, LIGHT, GROUND, WATER, FIRE, WIND, GOD, NONE
    }//暗光地水炎风神无
    
    public enum CardType {
        ORIGIN, EFECT, RITE, FUSION, HOMOLOGY, EXCESS, CONNECT, RAMIFICATION
    }//通常、效果、仪式、融合、同调、超量、连接、衍生物
}
