package card.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Table("YuGiOhCard")
public class yugiohCard {
    
    @Id
    private Long id;//主键

    private String name;//名字

    private boolean gradientColor = false;//是否采用渐变色

    private CardCatalog cardCatalog;//卡片大分类

    private ElementType elementType;//元素类型

    private CardType cardtype;//卡牌类型
    
    @Transient
    private skill skill;//技能

    private Long skillId;//技能Id

    @Transient
    private innerPrint print;//图片

    private Long printId;//图片Id

    //可选项
    private String designer;//设计师
    private String copyright;//版权
    private String number;//编号

    @Max(value = 13, message = "星级最高为13")
    @Min(value = 0, message = "星级最低为0")
    private int stars = 0;//星级

    //对于攻击力和防御力,-1为?,-2为无穷
    @Min(value = -2, message = "-1表示?,-2表示无穷大,不存在其他的负数攻击力")
    private int atk = -1;//攻击力

    @Min(value = -2, message = "-1表示?,-2表示无穷大,不存在其他的负数防御力")
    private int def = -1;//防御力

    public enum CardCatalog {
        MONSTER, MAGIC, TRAP, SOULPENDULUM
    }//怪兽、魔法、陷阱、灵摆

    public enum ElementType {
        DARK, LIGHT, GROUND, WATER, FIRE, WIND, GOD, NONE
    }//暗光地水炎风神无
    
    public enum CardType {
        ORIGIN, EFECT, RITE, FUSION, HOMOLOGY, EXCESS, CONNECT, RAMIFICATION
    }//通常、效果、仪式、融合、同调、超量、连接、衍生物

    public void setSkill(skill s) {
        this.skill = s;
        if(s.getId() != null) this.setSkillId(s.getId());
    }

    public void setPrint(innerPrint p) {
        this.print = p;
        if(p.getId() != null) this.setPrintId(p.getId());
    }
}
