package card.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.abort;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import card.data.cardOrderRepository;
import card.data.printForShowRepository;
import card.data.sanguoshaCardRepository;
import card.data.skillRepository;
import card.data.yugiohCardRepository;
import card.domain.cardOrder;
import card.domain.printForShow;
import card.domain.sanguoshaCard;
import card.domain.skill;
import card.domain.yugiohCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
public class AggregationServiceTest {

    @MockBean
    private cardOrderRepository cardOrderRepo;

    @MockBean
    private sanguoshaCardRepository sanguoshaCardRepo;

    @MockBean
    private yugiohCardRepository yugiohCardRepo;

    @MockBean
    private printForShowRepository printForShowRepo;

    @MockBean
    private skillRepository skillRepo;

    private sanguoshaCardAggregateService sanguoshaCardAggregator;
    private yugiohCardAggregateService yugiohCardAggregator;
    private cardOrderAggregateService cardOrderAggregator;

    @BeforeAll
    public static void setupAll() {

        TestConfig.setup();
    }

    @BeforeEach
    public void setupEach() {

        this.sanguoshaCardAggregator = new sanguoshaCardAggregateService(printForShowRepo, skillRepo, sanguoshaCardRepo);
        this.yugiohCardAggregator = new yugiohCardAggregateService(yugiohCardRepo, skillRepo, printForShowRepo);
        this.cardOrderAggregator = new cardOrderAggregateService(cardOrderRepo, sanguoshaCardAggregator, yugiohCardAggregator);

        Set<String> ids = new HashSet<>();
        ids.add("1"); ids.add("2");

        //初始化插画仓库行为
        int n1 = TestConfig.getPrintForShows().size();
        for(int i = 0; i < n1; i++) {
            when(printForShowRepo.save(TestConfig.getPrintForShows().get(i))).thenReturn(Mono.just(TestConfig.getPrintForShows().get(i)));
            when(printForShowRepo.findById(String.valueOf(i + 1))).thenReturn(Mono.just(TestConfig.getPrintForShows().get(i)));
        }
        when(printForShowRepo.findAll()).thenReturn(Flux.fromIterable(TestConfig.getPrintForShows()));
        when(printForShowRepo.findAllById(ids)).thenReturn(Flux.fromIterable(TestConfig.getPrintForShows()));

        //初始化技能仓库行为
        int n2 = TestConfig.getSkills().size();
        for(int i = 0; i < n2; i++) {
            when(skillRepo.save(TestConfig.getSkills().get(i))).thenReturn(Mono.just(TestConfig.getSkills().get(i)));
            when(skillRepo.findById(String.valueOf(i + 1))).thenReturn(Mono.just(TestConfig.getSkills().get(i)));
        }
        when(skillRepo.saveAll(TestConfig.getSkills())).thenReturn(Flux.fromIterable(TestConfig.getSkills()));
        when(skillRepo.findAllById(ids)).thenReturn(Flux.fromIterable(TestConfig.getSkills()));
        when(skillRepo.findAll()).thenReturn(Flux.fromIterable(TestConfig.getSkills()));

        //初始化三国杀卡牌仓库行为
        int n3 = TestConfig.getSanguoshaCards().size();
        for(int i = 0; i < n3; i++) {
            when(sanguoshaCardRepo.save(TestConfig.getSanguoshaCards().get(i))).thenReturn(Mono.just(TestConfig.getSanguoshaCards().get(i)));
            when(sanguoshaCardRepo.findById(String.valueOf(i + 1))).thenReturn(Mono.just(TestConfig.getSanguoshaCards().get(i)));
        }
        when(sanguoshaCardRepo.saveAll(Flux.fromIterable(TestConfig.getSanguoshaCards()))).thenReturn(Flux.fromIterable(TestConfig.getSanguoshaCards()));
        when(sanguoshaCardRepo.findAllById(ids)).thenReturn(Flux.fromIterable(TestConfig.getSanguoshaCards()));
        when(sanguoshaCardRepo.findAll()).thenReturn(Flux.fromIterable(TestConfig.getSanguoshaCards()));
        
        //初始化订单仓库行为
        when(cardOrderRepo.save(TestConfig.getOrder())).thenReturn(Mono.just(TestConfig.getOrder()));
        when(cardOrderRepo.findById("1")).thenReturn(Mono.just(TestConfig.getOrder()));

        //初始化游戏王卡牌仓库行为
        //TODO:
    }

    @Test
    public void testSanguoshaCardAggregator() {

        List<sanguoshaCard> cards = TestConfig.getSanguoshaCards();
        List<printForShow> prints = TestConfig.getPrintForShows();
        List<skill> skills = TestConfig.getSkills();

        Set<String> records = new HashSet<>();
        assertEquals(cards.size(), 2);

        Flux<sanguoshaCard> c1 = sanguoshaCardAggregator.saveAll(Flux.fromIterable(cards))
                .doOnNext(card -> records.add(card.getId()));
        StepVerifier.create(c1).expectNextCount(2).verifyComplete();

        //验证存三国杀卡牌是否将插画和技能存入数据库
        StepVerifier.create(printForShowRepo.findAll())
        .expectNextMatches(p -> prints.contains(p))
        .expectNextMatches(p -> prints.contains(p))
        .verifyComplete();

        StepVerifier.create(skillRepo.findAll())
        .expectNextMatches(s -> skills.contains(s))
        .expectNextMatches(s -> skills.contains(s))
        .verifyComplete();

        //验证三国杀卡牌数据库
        Flux<sanguoshaCard> c2 = Flux.fromIterable(records)
                .flatMap(id -> sanguoshaCardAggregator.findById(id));
        StepVerifier.create(c2)
        .expectNextMatches(card -> cards.contains(card))
        .expectNextMatches(card -> cards.contains(card))
        .verifyComplete();
    }

    @Test
    public void testYugiohCardAggregator() {
        // Set<yugiohCard> cards = this.generateYugiohCards();
        // Set<Long> records = new HashSet<>();
        // Flux<yugiohCard> c1 = yugiohCardAggregator.saveAll(Flux.fromIterable(cards))
        // .doOnNext(card -> records.add(card.getId())); // 聚合器保存相关卡牌，并记录相关Id
        // StepVerifier.create(c1).expectNextCount(3).verifyComplete();// 创建一系列验证动作

        // Flux<yugiohCard> c2 = Flux.fromIterable(records).flatMap(id ->
        // yugiohCardAggregator.findById(id));// 找到Id，并映射为卡牌类
        // StepVerifier.create(c2).expectNextMatches(card ->
        // cards.contains(card)).verifyComplete();
    }

    @Test
    public void testCardOrderAggregator() {
        cardOrder order = TestConfig.getOrder();
        List<sanguoshaCard> sCards = TestConfig.getSanguoshaCards();
        List<yugiohCard> yCards = TestConfig.getYugiohCards();
        List<printForShow> prints = TestConfig.getPrintForShows();
        List<skill> skills = TestConfig.getSkills();

        Mono<cardOrder> m1 = cardOrderAggregator.save(Mono.just(order));
        StepVerifier.create(m1).expectNextCount(1).verifyComplete();

        StepVerifier.create(sanguoshaCardRepo.findAll())
        .expectNextMatches(card -> sCards.contains(card))
        .expectNextMatches(card -> sCards.contains(card))
        .verifyComplete();

        StepVerifier.create(printForShowRepo.findAll())
        .expectNextMatches(p -> prints.contains(p))
        .expectNextMatches(p -> prints.contains(p))
        .verifyComplete();

        StepVerifier.create(skillRepo.findAll())
        .expectNextMatches(s -> skills.contains(s))
        .expectNextMatches(s -> skills.contains(s))
        .verifyComplete();

        StepVerifier.create(cardOrderAggregator.findById(order.getId()))
        .expectNext(order)
        .verifyComplete();
    }

}
