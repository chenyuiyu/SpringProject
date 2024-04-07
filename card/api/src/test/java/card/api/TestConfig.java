package card.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import card.domain.cardOrder;
import card.domain.printForShow;
import card.domain.sanguoshaCard;
import card.domain.skill;
import card.domain.yugiohCard;
import lombok.Getter;

@SpringBootConfiguration
@EnableAutoConfiguration
public class TestConfig {
    public static final String sanguoxiuUrl = "sanguosha/sanguoxiu/zzhead.jpg";// 三国秀
    public static final String qunUrl = "sanguosha/wujiang/【群】谋貂蝉.jpg";// 群

    public static final String monsterUrl = "yugioh/card/monster/blue-eyes.00ea670f.jpg";
    public static final String magicUrl = "yugioh/card/magic/fusion.jpg";
    public static final String trapUrl = "yugioh/card/trap/Holyreflector.jpg";
    public static final String soulpendulumUrl = "yugioh/card/soulpendulum/Heterochromaticeye.jpg";

    @Getter
    private static List<sanguoshaCard> sanguoshaCards = new ArrayList<>();

    @Getter
    private static List<yugiohCard> yugiohCards = new ArrayList<>();

    @Getter
    private static List<printForShow> printForShows = new ArrayList<>();

    @Getter
    private static List<skill> skills = new ArrayList<>();

    @Getter
    private static cardOrder order;

    public static void setup() {
        TestConfig.generateSanguoshaCards();
        TestConfig.generateYugiohCards();
        TestConfig.generateCardOrder();
    }

    private static void generateSanguoshaCards() {
        printForShow sanguoxiu = new printForShow(TestConfig.sanguoxiuUrl);
        printForShow qun = new printForShow(TestConfig.qunUrl);

        sanguoxiu.setId("1");
        qun.setId("2");
        TestConfig.printForShows.add(sanguoxiu);
        TestConfig.printForShows.add(qun);

        // 未知卡
        sanguoshaCard unknown = new sanguoshaCard(sanguoshaCard.countryType.valueOf("SHEN"));
        unknown.setId("1");
        unknown.setPrint(sanguoxiu);
        unknown.setName("玉米");
        unknown.setTitle("谦者不名");
        TestConfig.sanguoshaCards.add(unknown);

        // 谋貂蝉
        sanguoshaCard qunWuJiang = new sanguoshaCard(sanguoshaCard.countryType.valueOf("QUN"));
        qunWuJiang.setPrint(qun);
        qunWuJiang.setName("谋貂蝉");
        qunWuJiang.setTitle("离间计");

        List<skill> arr = Arrays.asList(
                new skill("离间", "出牌阶段限一次，你可以选择至少两名其他角色并弃置X-1张牌（X为你选择的角色数），他们依次对逆时针最近座次的你选择的另一名角色视为使用一张【决斗】。"),
                new skill("闭月", "锁定技，结束阶段，你摸X张牌（X为本回合受到伤害的角色数+1，至多为4）。"));

        arr.get(0).setId("1");
        arr.get(1).setId("2");
        TestConfig.skills.addAll(arr);

        qunWuJiang.setId("2");
        qunWuJiang.addSkills(arr);
        qunWuJiang.setMaxBlood(3);
        qunWuJiang.setPrinter("M云涯");
        qunWuJiang.setNumber("MG.QUN 003");
        qunWuJiang.setCopyright("@谋攻篇-识");
        qunWuJiang.setPrint(qun);
        qunWuJiang.setSkills(new ArrayList<>());
        qunWuJiang.addSkills(arr);
        TestConfig.sanguoshaCards.add(qunWuJiang);

    }

    private static void generateYugiohCards() {
        // printForShow monster = new printForShow(TestConfig.monsterUrl);
        // printForShow magic = new printForShow(TestConfig.magicUrl);

        // yugiohCard blueeyewhitedragon = new yugiohCard();
        // blueeyewhitedragon.setCardCatalog(yugiohCard.CardCatalog.valueOf("MONSTER"));
        // blueeyewhitedragon.setElementType(yugiohCard.ElementType.valueOf("LIGHT"));
        // blueeyewhitedragon.setCardtype(yugiohCard.CardType.valueOf("ORIGIN"));

        // printForShowRepo.save(monster).subscribe(res -> {
        // blueeyewhitedragon.setPrint(res);
        // });

        // blueeyewhitedragon.setName("青眼白龙");
        // blueeyewhitedragon.setGradientColor(true);
        // skillRepo.save(new skill("龙族/通常",
        // "以高攻击力著称的传说之龙。任何对手都能将之粉碎，其破坏力不可估量。")).subscribe(res -> {
        // blueeyewhitedragon.setSkill(res);
        // });
        // blueeyewhitedragon.setAtk(3000);
        // blueeyewhitedragon.setDef(2500);
        // blueeyewhitedragon.setStars(8);
        // blueeyewhitedragon.setDesigner("集英社");
        // blueeyewhitedragon.setNumber("89631139");
        // blueeyewhitedragon.setCopyright("@1996 KAZUKI TAKAHASHI");
        // yugiohCardRepo.save(blueeyewhitedragon);

        // yugiohCard fusion = new yugiohCard();
        // fusion.setCardCatalog(yugiohCard.CardCatalog.valueOf("MAGIC"));
        // fusion.setElementType(yugiohCard.ElementType.valueOf("NONE"));
        // fusion.setCardtype(yugiohCard.CardType.valueOf("EFFECT"));
        // printForShowRepo.save(magic).subscribe(res -> {
        // fusion.setPrint(res);
        // });

        // fusion.setName("融合");
        // skillRepo.save(new skill("",
        // "①：自己的手卡·场上的怪兽作为融合素材，把１只融合怪兽融合召唤。")).subscribe(res -> {
        // fusion.setSkill(res);
        // });
        // fusion.setDesigner("集英社");
        // fusion.setNumber("24094653");
        // fusion.setCopyright("@1996 KAZUKI TAKAHASHI");

        //Set<yugiohCard> res = new HashSet<>();
        // res.add(blueeyewhitedragon);
        // res.add(fusion);
        //return res;
    }

    private static void generateCardOrder() {
        cardOrder order = new cardOrder();
        List<sanguoshaCard> c1 = TestConfig.sanguoshaCards;
        List<yugiohCard> c2 = TestConfig.yugiohCards;

        order.setId("1");
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

        TestConfig.order = order;
    }
}
