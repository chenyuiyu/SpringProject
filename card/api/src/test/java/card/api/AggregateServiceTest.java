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
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class AggregateServiceTest {

    @MockBean
    private cardOrderRepository cardOrderRepo;

    @MockBean
    private sanguoshaCardRepository sanguoshaCardRepo;

    @MockBean
    private yugiohCardRepository yugiohCardRepo;

    @MockBean
    private innerPrintRepository innerPrintRepo;

    @MockBean
    private skillRepository skillRepo;

    private sanguoshaCardAggregateService sanguoshaCardAggregator;
    private yugiohCardAggregateService yugiohCardAggregator;
    private cardOrderAggregateService cardOrderAggregator;

    @BeforeAll
    public static void setupAll() {

        TestConfig.setup();
    }

    @SuppressWarnings("null")
    @BeforeEach
    public void setupEach() {

        this.sanguoshaCardAggregator = new sanguoshaCardAggregateService(innerPrintRepo, skillRepo, sanguoshaCardRepo);
        this.yugiohCardAggregator = new yugiohCardAggregateService(yugiohCardRepo, skillRepo, innerPrintRepo);
        this.cardOrderAggregator = new cardOrderAggregateService(cardOrderRepo, sanguoshaCardAggregator, yugiohCardAggregator);

        Set<Long> ids = new HashSet<>();
        ids.add(1l); ids.add(2l);

        int n1 = TestConfig.getInInnerPrints().size();
        for(int i = 0; i < n1; i++) {
            when(innerPrintRepo.save(TestConfig.getInInnerPrints().get(i))).thenReturn(Mono.just(TestConfig.getOutInnerPrints().get(i)));
            when(innerPrintRepo.findById((long)(i + 1))).thenReturn(Mono.just(TestConfig.getOutInnerPrints().get(i)));
        }

        int n2 = TestConfig.getInSkills().size();
        for(int i = 0; i < n2; i++) {
            when(skillRepo.findById((long)(i + 1))).thenReturn(Mono.just(TestConfig.getOutSkills().get(i)));
        }
        when(skillRepo.saveAll(TestConfig.getInSkills())).thenReturn(Flux.fromIterable(TestConfig.getOutSkills()));
        when(skillRepo.findAllById(ids)).thenReturn(Flux.fromIterable(TestConfig.getOutSkills()));

        int n3 = TestConfig.getInSanguoshaCards().size();
        for(int i = 0; i < n3; i++) {
            when(sanguoshaCardRepo.findById((long)(i + 1))).thenReturn(Mono.just(TestConfig.getOutSanguoshaCards().get(i)));
            when(sanguoshaCardRepo.save(TestConfig.getInSanguoshaCards().get(i))).thenReturn(Mono.just(TestConfig.getOutSanguoshaCards().get(i)));
        }
        when(sanguoshaCardRepo.saveAll(Flux.fromIterable(TestConfig.getInSanguoshaCards()))).thenReturn(Flux.fromIterable(TestConfig.getOutSanguoshaCards()));
        
        when(cardOrderRepo.save(TestConfig.getInorder())).thenReturn(Mono.just(TestConfig.getOutorder()));
        when(cardOrderRepo.findById(1l)).thenReturn(Mono.just(TestConfig.getOutorder()));
    }

    @Test
    public void testSanguoshaCardAggregator() {

        List<sanguoshaCard> cards = TestConfig.getInSanguoshaCards();
        List<sanguoshaCard> outCards = TestConfig.getOutSanguoshaCards();
        Set<Long> records = new HashSet<>();
        assertEquals(cards.size(), 2);

        Flux<sanguoshaCard> c1 = sanguoshaCardAggregator.saveAll(Flux.fromIterable(cards))
                .doOnNext(card -> records.add(card.getId()));
        StepVerifier.create(c1).expectNextCount(2).verifyComplete();

        Flux<sanguoshaCard> c2 = Flux.fromIterable(records)
                .flatMap(id -> sanguoshaCardAggregator.findById(id));
        StepVerifier.create(c2)
        .expectNextMatches(card -> outCards.contains(card))
        .expectNextMatches(card -> outCards.contains(card))
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
        cardOrder order = TestConfig.getInorder();
        cardOrder outOrder = TestConfig.getOutorder();
        Mono<cardOrder> m1 = cardOrderAggregator.save(Mono.just(order)).doOnNext(x -> order.setId(x.getId()));
        StepVerifier.create(m1).expectNextCount(1).verifyComplete();

        StepVerifier.create(cardOrderAggregator.findById(order.getId()))
                .expectNext(outOrder)
                .verifyComplete();
    }

}
