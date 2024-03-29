package card.data;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import card.data.config.dataConfig;
import card.domain.innerPrint;
import card.domain.sanguoshaCard;
import card.domain.skill;


@Service
public class dataLoader {
    
    @Autowired
    private innerPrintRepository innerPrintRepo;

    @Autowired
    private sanguoshaCardRepository sanguoshaCardRepo;

    @Autowired
    private yugiohCardRepository yugiohCardRepo;

    @Autowired
    private skillRepository skillRepo;

    @Autowired
    private dataConfig dataconfig;

    public void loadData(String... args) throws Exception {
        loadSanGuoSha();
        loadYuGiOh();
    }

    private void loadSanGuoSha() {
        innerPrint sanguoxiu = new innerPrint(dataconfig.sanguoxiuUrl);
        innerPrint qun = new innerPrint(dataconfig.qunUrl);
        innerPrint shen = new innerPrint(dataconfig.shenUrl);
        innerPrint shu = new innerPrint(dataconfig.shuUrl);
        innerPrint wei = new innerPrint(dataconfig.weiUrl);
        innerPrint wu = new innerPrint(dataconfig.wuUrl);

        innerPrintRepo.deleteAll().subscribe();
        skillRepo.deleteAll().subscribe();
        sanguoshaCardRepo.deleteAll().subscribe();
        
        //未知卡
        sanguoshaCard unknown = new sanguoshaCard(sanguoshaCard.countryType.valueOf("SHEN"));

        innerPrintRepo.save(sanguoxiu).subscribe(res -> {unknown.setPrint(res);}); 

        unknown.setName("玉米");
        unknown.setTitle("谦者不名");
        
        sanguoshaCard weiWuJiang = new sanguoshaCard(sanguoshaCard.countryType.valueOf("WEI"));

        innerPrintRepo.save(wei).subscribe(res -> {weiWuJiang.setPrint(res);});

        weiWuJiang.setName("戏志才");
        weiWuJiang.setTitle("负俗的天才");
        
        List<skill> arr1 = Arrays.asList(
            new skill("天妒", "当你的判定牌生效后，你可以获得此牌。"),
            new skill("先辅", "锁定技，游戏开始时，你选择一名其他角色，当其受到伤害后，你受到等量的伤害；当其回复体力后，你回复等量的体力。"),
            new skill("筹策", "当你受到1点伤害后，你可以进行判定，若结果为：黑色，你弃置一名角色区域里的一张牌；红色，你令一名角色摸一张牌（先辅的角色摸两张）。")
        );

        skillRepo.saveAll(arr1).subscribe(res -> {weiWuJiang.addSkill(res);});

        weiWuJiang.setMaxBlood(3);
        weiWuJiang.setPrinter("眉毛子");
        weiWuJiang.setNumber("WEI 055");
        weiWuJiang.setCopyright("DIY");

        //鲍三娘
        sanguoshaCard shuWuJiang = new sanguoshaCard(sanguoshaCard.countryType.valueOf("SHU"));
        innerPrintRepo.save(shu).subscribe(res -> {shuWuJiang.setPrint(res);});

        shuWuJiang.setName("鲍三娘");
        shuWuJiang.setTitle("南中武娘");

        List<skill> arr2 = Arrays.asList(
            new skill("武娘", "当你使用或打出【杀】时，你可以获得一名其他角色的一张牌。若如此做，其摸一张牌。若你发动过“许身”，姓名为关索的角色摸一张牌。"),
            new skill("许身", "限定技，当你进入濒死状态后，你可以回复1点体力并获得“镇南”，然后当你脱离濒死状态后，若关索不在场，你可令一名其他角色选择是否用关索代替其武将并令其摸三张牌。"),
            new skill("☆镇南", "当普通锦囊牌指定第一个目标后，若目标对应的角色数大于1，你可以对一名其他角色造成1点伤害。")
        );

        skillRepo.saveAll(arr2).subscribe(res -> {shuWuJiang.addSkill(res);});

        shuWuJiang.setMaxBlood(3);
        shuWuJiang.setPrinter("DH");
        shuWuJiang.setNumber("SHU 070");
        shuWuJiang.setCopyright("DIY");

        //孙鲁班
        sanguoshaCard wuWuJiang = new sanguoshaCard(sanguoshaCard.countryType.valueOf("WU"));
        innerPrintRepo.save(wu).subscribe(res -> {wuWuJiang.setPrint(res);});

        wuWuJiang.setName("孙鲁班");
        wuWuJiang.setTitle("为虎作伥");

        List<skill> arr3 = Arrays.asList(
            new skill("谮毁", "出牌阶段限一次，当你使用【杀】或黑色普通锦囊牌指定唯一目标时，你可令能成为此牌目标的另一名角色选择一项：1.交给你一张牌，然后代替你成为此牌的使用者；2.也成为此牌的目标。"),
            new skill("骄矜", "当你受到男性角色造成的伤害时，你可以弃置一张装备牌，然后此伤害-1。")
        );

        skillRepo.saveAll(arr3).subscribe(res -> {wuWuJiang.addSkill(res);});

        wuWuJiang.setMaxBlood(3);
        wuWuJiang.setPrinter("FOOLTOWN");
        wuWuJiang.setNumber("YJ307");
        wuWuJiang.setCopyright("@一将成名2014");

        //谋貂蝉
        sanguoshaCard qunWuJiang = new sanguoshaCard(sanguoshaCard.countryType.valueOf("QUN"));
        innerPrintRepo.save(qun).subscribe(res -> {qunWuJiang.setPrint(res);});
        
        qunWuJiang.setName("谋貂蝉");
        qunWuJiang.setTitle("离间计");

        List<skill> arr4 = Arrays.asList(
            new skill("离间", "出牌阶段限一次，你可以选择至少两名其他角色并弃置X-1张牌（X为你选择的角色数），他们依次对逆时针最近座次的你选择的另一名角色视为使用一张【决斗】。"),
            new skill("闭月", "锁定技，结束阶段，你摸X张牌（X为本回合受到伤害的角色数+1，至多为4）。")
        );
        skillRepo.saveAll(arr4).subscribe(res -> {qunWuJiang.addSkill(res);});

        qunWuJiang.setMaxBlood(3);
        qunWuJiang.setPrinter("M云涯");
        qunWuJiang.setNumber("MG.QUN 003");
        qunWuJiang.setCopyright("@谋攻篇-识");

        //神荀彧
        sanguoshaCard shenWuJiang = new sanguoshaCard(sanguoshaCard.countryType.valueOf("SHEN"));
        innerPrintRepo.save(shen).subscribe(res -> {shenWuJiang.setPrint(res);});
        

        shenWuJiang.setName("神荀彧");
        shenWuJiang.setTitle("洞心先识");
        List<skill> arr5 = Arrays.asList(
            new skill("天佐", "锁定技，游戏开始时，你将八张【奇正相生】加入牌堆。【奇正相生】对你无效。"),
            new skill("灵策", "当一名角色使用非虚拟且非转化的锦囊牌时，若此牌的牌名属于智囊牌名、“定汉”已记录的牌名或【奇正相生】，你摸一张牌。"),
            new skill("定汉", "每种牌名限一次，当你成为锦囊牌的目标时，你记录此牌名，然后取消之。回合开始时，你可以在“定汉”记录中增加或移除一种锦囊牌牌名。")
        );

        skillRepo.saveAll(arr5).subscribe(res -> {shenWuJiang.addSkill(res);});

        shenWuJiang.setMaxBlood(3);
        shenWuJiang.setPrinter("枭瞳");
        shenWuJiang.setNumber("LE019");
        shenWuJiang.setCopyright("@始计篇·智");

        sanguoshaCardRepo.saveAll(Arrays.asList(unknown, weiWuJiang, shuWuJiang, wuWuJiang, qunWuJiang, shenWuJiang)).subscribe();
    }

    private void loadYuGiOh() {

    }
}