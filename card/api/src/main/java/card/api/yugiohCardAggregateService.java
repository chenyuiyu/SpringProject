package card.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import card.data.printForShowRepository;
import card.data.skillRepository;
import card.data.yugiohCardRepository;
import card.domain.skill;
import card.domain.yugiohCard;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class yugiohCardAggregateService {

    @Autowired
    private yugiohCardRepository yugiohCardRepo;

    @Autowired
    private skillRepository skillRepo;

    @Autowired
    private printForShowRepository printForShowRepo;

    // @SuppressWarnings("null")
    //@Transactional
    public Mono<yugiohCard> save(Mono<yugiohCard> card) {
        // return card.flatMap(
        // c -> {
        // if (c.getPrint() == null)
        // return Mono.just(c);
        // return printForShowRepo.save(c.getPrint()).map(
        // p -> {
        // c.setPrint(p);
        // return c;
        // });
        // }).flatMap(
        // c -> {
        // skill skilld = c.getSkill();
        // if (skilld == null)
        // return Mono.just(c);
        // return skillRepo.save(skilld).map(
        // s -> {
        // c.setSkill(s);
        // return c;
        // });
        // })
        // .flatMap(yugiohCardRepo::save);
        return Mono.empty();
    }

    // @SuppressWarnings("null")
    public Mono<yugiohCard> findById(Long id) {
        // if (id == null)
        // return Mono.empty();
        // return yugiohCardRepo.findById(id).flatMap(
        // card -> {
        // if (card.getPrint() == null)
        // return Mono.just(card);
        // return printForShowRepo.findById(card.getPrintId()).map(
        // p -> {
        // card.setPrint(p);
        // return card;
        // });
        // }).flatMap(
        // card -> {
        // if (card.getSkillId() == null)
        // return Mono.just(card);
        // return skillRepo.findById(card.getSkillId()).map(
        // s -> {
        // card.setSkill(s);
        // return card;
        // });
        // });
        return Mono.empty();
    }

    public Flux<yugiohCard> saveAll(Flux<yugiohCard> cards) {
        // return cards.flatMap(card -> {
        // return this.save(Mono.just(card));
        // });
        return cards;
    }
}
