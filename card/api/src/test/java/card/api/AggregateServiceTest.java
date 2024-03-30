package card.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.test.annotation.DirtiesContext;
import card.data.cardOrderRepository;
import card.data.innerPrintRepository;
import card.data.sanguoshaCardRepository;
import card.data.skillRepository;
import card.data.yugiohCardRepository;
import card.data.domain.cardOrder;
import card.data.domain.innerPrint;
import card.data.domain.sanguoshaCard;
import card.data.domain.skill;
import card.data.domain.yugiohCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
@DirtiesContext
public class AggregateServiceTest {

    private cardOrderRepository cardOrderRepo;
    private sanguoshaCardRepository sanguoshaCardRepo;
    private yugiohCardRepository yugiohCardRepo;
    private innerPrintRepository innerPrintRepo;
    private skillRepository skillRepo;

    private sanguoshaCardAggregateService sanguoshaCardAggregator;
    private yugiohCardAggregateService yugiohCardAggregator;
    private cardOrderAggregateService cardOrderAggregator;

    @Autowired
    public AggregateServiceTest(sanguoshaCardRepository sanguoshaCardRepo, yugiohCardRepository yugiohCardRepo,
    cardOrderRepository cardOrderRepo, innerPrintRepository innerPrintRepo, skillRepository skillRepo) {
        this.sanguoshaCardRepo = sanguoshaCardRepo;
        this.yugiohCardRepo = yugiohCardRepo;
        this.innerPrintRepo = innerPrintRepo;
        this.cardOrderRepo = cardOrderRepo;
        this.skillRepo = skillRepo;
    }
    
    @BeforeEach
    public void setup() {
        sanguoshaCardAggregator = new sanguoshaCardAggregateService(innerPrintRepo, skillRepo, sanguoshaCardRepo);
        yugiohCardAggregator = new yugiohCardAggregateService(yugiohCardRepo, innerPrintRepo);
        cardOrderAggregator = new cardOrderAggregateService(cardOrderRepo, sanguoshaCardAggregator, yugiohCardAggregator);
    }

    @Test
    public void testSanguoshaCardAggregator() {

        Set<sanguoshaCard> cards = this.generateSanguoshaCards();
        Set<Long> records = new HashSet<>();

        Flux<sanguoshaCard> c1 = sanguoshaCardAggregator.saveAll(Flux.fromIterable(cards))
        .doOnNext(card -> records.add(card.getId()));
        StepVerifier.create(c1).expectNextCount(2).verifyComplete();
    
        Flux<sanguoshaCard> c2 = Flux.fromIterable(records)
        .flatMap(id -> sanguoshaCardAggregator.findById(id));
        StepVerifier.create(c2).expectNextMatches(card -> cards.contains(card)).verifyComplete();
    }

    @Test
    public void testYugiohCardAggregator() {
        //TODO:
    }

    @Test
    public void testCardOrderAggregator() {
        cardOrder order = this.generateCardOrder();
        
        Mono<cardOrder> m1 = cardOrderAggregator.save(Mono.just(order)).doOnNext(x -> order.setId(x.getId()));
        StepVerifier.create(m1).expectNextCount(1).verifyComplete();

        StepVerifier.create(cardOrderAggregator.findById(order.getId()))
        .expectNext(order)
        .verifyComplete();
    }

    private Set<sanguoshaCard> generateSanguoshaCards() {
        innerPrint sanguoxiu = new innerPrint(TestConfig.sanguoxiuUrl);
        innerPrint qun = new innerPrint(TestConfig.qunUrl);

        //未知卡
        sanguoshaCard unknown = new sanguoshaCard(sanguoshaCard.countryType.valueOf("SHEN"));
        unknown.setPrint(sanguoxiu); 
        unknown.setName("玉米");
        unknown.setTitle("谦者不名");
        
        //谋貂蝉
        sanguoshaCard qunWuJiang = new sanguoshaCard(sanguoshaCard.countryType.valueOf("QUN"));
        qunWuJiang.setPrint(qun);
        qunWuJiang.setName("谋貂蝉");
        qunWuJiang.setTitle("离间计");
        List<skill> arr = Arrays.asList(
            new skill("离间", "出牌阶段限一次，你可以选择至少两名其他角色并弃置X-1张牌（X为你选择的角色数），他们依次对逆时针最近座次的你选择的另一名角色视为使用一张【决斗】。"),
            new skill("闭月", "锁定技，结束阶段，你摸X张牌（X为本回合受到伤害的角色数+1，至多为4）。")
        );
        qunWuJiang.addSkills(arr);
        qunWuJiang.setMaxBlood(3);
        qunWuJiang.setPrinter("M云涯");
        qunWuJiang.setNumber("MG.QUN 003");
        qunWuJiang.setCopyright("@谋攻篇-识");

        Set<sanguoshaCard> res = new HashSet<>();
        res.add(unknown);
        res.add(qunWuJiang);
        return res;
    }

    private Set<yugiohCard> generateYugiohCards() {
        //TODO
        Set<yugiohCard> res = new HashSet<>();
        return res;
    }

    private cardOrder generateCardOrder() {
        cardOrder order = new cardOrder();
        Set<sanguoshaCard> c1 = this.generateSanguoshaCards();
        Set<yugiohCard> c2 = this.generateYugiohCards();

        order.addSanGuoShaCards(c1);
        order.addYuGiOhCards(c2);
        order.setPrices(100l);
        order.setName("CYY");
        order.setProvince("Guangdong");
        order.setCity("Maoming");
        order.setState("Maonanqv");
        order.setCcNumber("110");
        order.setDetailedLocation("police office");
        order.setOrderDescription("Please send the package to police office.");
        
        return order;
    }
}
