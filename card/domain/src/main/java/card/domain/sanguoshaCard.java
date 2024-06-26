package card.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Document
public class sanguoshaCard {

    @Id
    private String id; //主键

    private Double frameH = 88.0;//边框高度
    private Double frameW = 63.0;//边框宽度

    private @NonNull countryType country;//国家

    private Integer blood = 4;//血量
    private Integer maxBlood = 4;//血量上限
    private Boolean unEqualBlood = false;//血量与上限不等
    
    private String title;//武将称号

    private String name;//武将名

    //插画相关信息
    private Double X = 0.0;//X轴方向位移
    private Double Y = 0.0;//Y轴方向位移
    private Double scale = 100.0;//缩放
    private printForShow print;//武将原画
    
    //可选项
    private String printer;//画师
    private String copyright;//版权
    private String number;//编号

    private List<skill> skills = new ArrayList<>();//武将技能

    public enum countryType {
        WEI, SHU, WU, QUN, SHEN
    }//魏蜀吴群神晋

    public void setMaxBlood(int maxBlood) {
        this.maxBlood = maxBlood;
        if(this.blood > maxBlood || !this.getUnEqualBlood()) this.blood = maxBlood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
        if(this.maxBlood < blood || !this.getUnEqualBlood()) this.maxBlood = blood;
    }

    public void addSkill(skill s) {
        if(s != null) skills.add(s);
    }

    public void addSkills(Iterable<skill> skills) {
        for(skill s : skills) {
            this.addSkill(s);
        }
    }
}
