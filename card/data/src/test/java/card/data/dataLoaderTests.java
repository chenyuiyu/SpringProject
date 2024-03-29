package card.data;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import card.data.config.dataConfig;
import card.domain.innerPrint;
import card.domain.sanguoshaCard;
import card.domain.skill;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
public class dataLoaderTests {
    @Autowired
    innerPrintRepository innerPrintRepo;

    @Autowired
    sanguoshaCardRepository sanguoshaCardRepo;

    @Autowired
    skillRepository skillRepo;

    private Set<innerPrint> printSet;
    private Set<skill> skillSet;
    private Set<sanguoshaCard> sanguoshacardSet;

    @BeforeEach
    public void setup() {

        this.printSet = new HashSet<>();
        this.skillSet = new HashSet<>();
        this.sanguoshacardSet = new HashSet<>();

        innerPrint sanguoxiu = new innerPrint(TestConfig.sanguoxiuUrl);
        innerPrint qun = new innerPrint(TestConfig.qunUrl);
        innerPrint shen = new innerPrint(TestConfig.shenUrl);
        innerPrint shu = new innerPrint(TestConfig.shuUrl);
        innerPrint wei = new innerPrint(TestConfig.weiUrl);
        innerPrint wu = new innerPrint(TestConfig.wuUrl);

        innerPrintRepo.deleteAll().subscribe();
        skillRepo.deleteAll().subscribe();
        sanguoshaCardRepo.deleteAll().subscribe();
        
        printSet.addAll(Arrays.asList(sanguoxiu, qun, shen, shu, wei, wu));

        //未知卡
        sanguoshaCard unknown = new sanguoshaCard(sanguoshaCard.countryType.valueOf("SHEN"));

        Mono<innerPrint> p1 = innerPrintRepo.save(sanguoxiu).doOnNext(res -> unknown.setPrint(res)); 
        StepVerifier.create(p1).expectNext(sanguoxiu).verifyComplete();

        unknown.setName("玉米");
        unknown.setTitle("谦者不名");
        
        sanguoshaCard weiWuJiang = new sanguoshaCard(sanguoshaCard.countryType.valueOf("WEI"));

        Mono<innerPrint> p2 = innerPrintRepo.save(wei).doOnNext(res -> weiWuJiang.setPrint(res));
        StepVerifier.create(p2).expectNext(wei).verifyComplete();

        weiWuJiang.setName("戏志才");
        weiWuJiang.setTitle("负俗的天才");
        
        List<skill> arr1 = Arrays.asList(
            new skill("天妒", "当你的判定牌生效后，你可以获得此牌。"),
            new skill("先辅", "锁定技，游戏开始时，你选择一名其他角色，当其受到伤害后，你受到等量的伤害；当其回复体力后，你回复等量的体力。"),
            new skill("筹策", "当你受到1点伤害后，你可以进行判定，若结果为：黑色，你弃置一名角色区域里的一张牌；红色，你令一名角色摸一张牌（先辅的角色摸两张）。")
        );
        skillSet.addAll(arr1);

        Flux<skill> s1 = skillRepo.saveAll(arr1).doOnNext(res -> weiWuJiang.addSkill(res));
        StepVerifier.create(s1).expectNextCount(3).verifyComplete();

        weiWuJiang.setMaxBlood(3);
        weiWuJiang.setPrinter("眉毛子");
        weiWuJiang.setNumber("WEI 055");
        weiWuJiang.setCopyright("DIY");

        //鲍三娘
        sanguoshaCard shuWuJiang = new sanguoshaCard(sanguoshaCard.countryType.valueOf("SHU"));

        Mono<innerPrint> p3 = innerPrintRepo.save(shu).doOnNext(res -> shuWuJiang.setPrint(res));
        StepVerifier.create(p3).expectNext(shu).verifyComplete();

        shuWuJiang.setName("鲍三娘");
        shuWuJiang.setTitle("南中武娘");

        List<skill> arr2 = Arrays.asList(
            new skill("武娘", "当你使用或打出【杀】时，你可以获得一名其他角色的一张牌。若如此做，其摸一张牌。若你发动过“许身”，姓名为关索的角色摸一张牌。"),
            new skill("许身", "限定技，当你进入濒死状态后，你可以回复1点体力并获得“镇南”，然后当你脱离濒死状态后，若关索不在场，你可令一名其他角色选择是否用关索代替其武将并令其摸三张牌。"),
            new skill("☆镇南", "当普通锦囊牌指定第一个目标后，若目标对应的角色数大于1，你可以对一名其他角色造成1点伤害。")
        );
        skillSet.addAll(arr2);

        Flux<skill> s2 = skillRepo.saveAll(arr2).doOnNext(res -> shuWuJiang.addSkill(res));
        StepVerifier.create(s2).expectNextCount(3).verifyComplete();

        shuWuJiang.setMaxBlood(3);
        shuWuJiang.setPrinter("DH");
        shuWuJiang.setNumber("SHU 070");
        shuWuJiang.setCopyright("DIY");

        //孙鲁班
        sanguoshaCard wuWuJiang = new sanguoshaCard(sanguoshaCard.countryType.valueOf("WU"));

        Mono<innerPrint> p4 = innerPrintRepo.save(wu).doOnNext(res -> wuWuJiang.setPrint(res));
        StepVerifier.create(p4).expectNext(wu).verifyComplete();

        wuWuJiang.setName("孙鲁班");
        wuWuJiang.setTitle("为虎作伥");

        List<skill> arr3 = Arrays.asList(
            new skill("谮毁", "出牌阶段限一次，当你使用【杀】或黑色普通锦囊牌指定唯一目标时，你可令能成为此牌目标的另一名角色选择一项：1.交给你一张牌，然后代替你成为此牌的使用者；2.也成为此牌的目标。"),
            new skill("骄矜", "当你受到男性角色造成的伤害时，你可以弃置一张装备牌，然后此伤害-1。")
        );
        skillSet.addAll(arr3);

        Flux<skill> s3 = skillRepo.saveAll(arr3).doOnNext(res -> wuWuJiang.addSkill(res));
        StepVerifier.create(s3).expectNextCount(2).verifyComplete();

        wuWuJiang.setMaxBlood(3);
        wuWuJiang.setPrinter("FOOLTOWN");
        wuWuJiang.setNumber("YJ307");
        wuWuJiang.setCopyright("@一将成名2014");

        //谋貂蝉
        sanguoshaCard qunWuJiang = new sanguoshaCard(sanguoshaCard.countryType.valueOf("QUN"));

        Mono<innerPrint> p5 = innerPrintRepo.save(qun).doOnNext(res -> qunWuJiang.setPrint(res));
        StepVerifier.create(p5).expectNext(qun).verifyComplete();

        qunWuJiang.setName("谋貂蝉");
        qunWuJiang.setTitle("离间计");

        List<skill> arr4 = Arrays.asList(
            new skill("离间", "出牌阶段限一次，你可以选择至少两名其他角色并弃置X-1张牌（X为你选择的角色数），他们依次对逆时针最近座次的你选择的另一名角色视为使用一张【决斗】。"),
            new skill("闭月", "锁定技，结束阶段，你摸X张牌（X为本回合受到伤害的角色数+1，至多为4）。")
        );
        skillSet.addAll(arr4);

        Flux<skill> s4 = skillRepo.saveAll(arr4).doOnNext(res -> qunWuJiang.addSkill(res));
        StepVerifier.create(s4).expectNextCount(2).verifyComplete();

        qunWuJiang.setMaxBlood(3);
        qunWuJiang.setPrinter("M云涯");
        qunWuJiang.setNumber("MG.QUN 003");
        qunWuJiang.setCopyright("@谋攻篇-识");

        //神荀彧
        sanguoshaCard shenWuJiang = new sanguoshaCard(sanguoshaCard.countryType.valueOf("SHEN"));

        Mono<innerPrint> p6 =  innerPrintRepo.save(shen).doOnNext(res -> shenWuJiang.setPrint(res));
        StepVerifier.create(p6).expectNext(shen).verifyComplete();

        shenWuJiang.setName("神荀彧");
        shenWuJiang.setTitle("洞心先识");
        List<skill> arr5 = Arrays.asList(
            new skill("天佐", "锁定技，游戏开始时，你将八张【奇正相生】加入牌堆。【奇正相生】对你无效。"),
            new skill("灵策", "当一名角色使用非虚拟且非转化的锦囊牌时，若此牌的牌名属于智囊牌名、“定汉”已记录的牌名或【奇正相生】，你摸一张牌。"),
            new skill("定汉", "每种牌名限一次，当你成为锦囊牌的目标时，你记录此牌名，然后取消之。回合开始时，你可以在“定汉”记录中增加或移除一种锦囊牌牌名。")
        );
        skillSet.addAll(arr5);

        Flux<skill> s5 = skillRepo.saveAll(arr5).doOnNext(res -> shenWuJiang.addSkill(res));
        StepVerifier.create(s5).expectNextCount(3).verifyComplete();

        shenWuJiang.setMaxBlood(3);
        shenWuJiang.setPrinter("枭瞳");
        shenWuJiang.setNumber("LE019");
        shenWuJiang.setCopyright("@始计篇·智");

        Flux<sanguoshaCard> c1 = sanguoshaCardRepo.saveAll(Arrays.asList(unknown, weiWuJiang, shuWuJiang, wuWuJiang, qunWuJiang, shenWuJiang));
        StepVerifier.create(c1).expectNextCount(6).verifyComplete();
        sanguoshacardSet.addAll(Arrays.asList(unknown, weiWuJiang, shuWuJiang, wuWuJiang, qunWuJiang, shenWuJiang));
    }

    @Test
    public void testInnerPrintRepository() {
        StepVerifier.create(innerPrintRepo.findAll())
        .recordWith(ArrayList::new)
        .thenConsumeWhile(x -> true)
        .consumeRecordedWith(
            arr -> {
                assertThat(arr).hasSize(6);
                for(innerPrint p : arr) {
                    assertThat(printSet).contains(p);
                }
            }
        ).verifyComplete();
    }

    @Test
    public void testSkillRepository() {
        StepVerifier.create(skillRepo.findAll())
        .recordWith(ArrayList::new)
        .thenConsumeWhile(x -> true)
        .consumeRecordedWith(
            arr -> {
                assertThat(arr).hasSize(13);
                for(skill s : arr) {
                    assertThat(skillSet).contains(s);
                }
            }
        ).verifyComplete();
    }

    @Test
    public void testSanGuoShaRepository() {
        
        Flux<sanguoshaCard> f = sanguoshaCardRepo.findAll()
        .flatMap(
            card -> {
                return innerPrintRepo.findById(card.getPrintId())
                    .doOnNext(p -> {card.setPrint(p);})
                    .thenReturn(card);
            }
        ).flatMap(
            card -> {
                card.setSkills(new ArrayList<>());
                return skillRepo.findAllById(card.getSkillIds())
                    .doOnNext(s -> {card.addSkill(s);})
                    .then(Mono.just(card));
            }
        );

        StepVerifier.create(f)
            .recordWith(ArrayList::new)
            .thenConsumeWhile(x -> true)
            .consumeRecordedWith(
                arr -> {
                    assertThat(arr).hasSize(6);
                    for(sanguoshaCard c : arr) {
                        assertThat(sanguoshacardSet).contains(c);
                    }
                } 
            ).verifyComplete();
    }
}
