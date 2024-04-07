package card.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Document
public class cardOrder {
    
    @Id
    private String id;//订单主键

    private List<sanguoshaCard> sanguoshaCards = new ArrayList<>();//三国杀卡牌集合

    private List<yugiohCard> yugiohCards = new ArrayList<>();//游戏王卡牌集合

    private Long prices;//订单总额

    private String name;//收件人姓名
    private String province;//省份
    private String city;//城市
    private String state;//区
    private String detailedLocation;//地址详细描述
    private String ccNumber;//联系电话
    private String OrderDescription;//订单描述信息(备注)

    public void addSanGuoShaCard(sanguoshaCard card) {
        if(card == null) return;
        this.sanguoshaCards.add(card);
    }

    public void addSanGuoShaCards(Iterable<sanguoshaCard> cards) {
        if(cards == null) return;
        for(sanguoshaCard card : cards) {
            this.addSanGuoShaCard(card);
        }
    }

    public void addYuGiOhCard(yugiohCard card) {
        if(card == null) return;
        this.yugiohCards.add(card);
    }

    public void addYuGiOhCards(Iterable<yugiohCard> cards) {
        if(cards == null) return;
        for(yugiohCard card : cards) {
            this.addYuGiOhCard(card);
        }
    }
}
