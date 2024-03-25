package card.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Document
public class sanguoshaCard {

    @Id
    private String id; //主键

    private Double frameH = 88.0;//边框高度
    private Double frameW = 63.0;//边框宽度

    @NotNull
    private countryType country;//国家

    private int blood = 4;//血量
    private int maxBlood = 4;//血量上限
    private boolean unEqualBlood = false;//血量与上限不等
    
    private String title;//武将称号

    private String name;//武将名

    private innerPrint picture;//武将原画
    
    //可选项
    private String printer;//画师
    private String copyright;//版权
    private String number;//编号

    public enum countryType {
        WEI, SHU, WU, QUN, SHEN, JIN
    }//魏蜀吴群神晋

    public void setMaxBlood(int maxBlood) {
        this.maxBlood = maxBlood;
        if(this.blood > maxBlood || !this.isUnEqualBlood()) this.setBlood(maxBlood);
    }

    public void setBlood(int blood) {
        this.blood = blood;
        if(this.maxBlood < blood || !this.isUnEqualBlood()) this.setMaxBlood(blood);
    }
}
