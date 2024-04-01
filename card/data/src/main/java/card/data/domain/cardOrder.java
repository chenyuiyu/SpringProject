package card.data.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@Table("CardOrder")
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class cardOrder {
    
    @Id
    private Long id;//订单主键

    @Transient
    private List<sanguoshaCard> sanguoshaCards = new ArrayList<>();//三国杀卡牌集合
    private Set<Long> sanguoshaCardIds = new HashSet<>();//三国杀卡牌主键集合

    @Transient
    private List<yugiohCard> yugiohCards = new ArrayList<>();//游戏王卡牌集合
    private Set<Long> yugiohCardIds = new HashSet<>();//游戏王卡牌主键集合

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
        if(card.getId() != null) this.sanguoshaCardIds.add(card.getId());
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
        if(card.getId() != null) this.yugiohCardIds.add(card.getId());
    }

    public void addYuGiOhCards(Iterable<yugiohCard> cards) {
        if(cards == null) return;
        for(yugiohCard card : cards) {
            this.addYuGiOhCard(card);
        }
    }
}
