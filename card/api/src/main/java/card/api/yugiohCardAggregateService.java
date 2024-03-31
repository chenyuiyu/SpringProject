package card.api;

import org.springframework.stereotype.Service;

import card.data.innerPrintRepository;
import card.data.skillRepository;
import card.data.yugiohCardRepository;
import card.data.domain.yugiohCard;
import card.data.domain.skill;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class yugiohCardAggregateService {

    private yugiohCardRepository yugiohCardRepo;
    private skillRepository skillRepo;
    private innerPrintRepository innerPrintRepo;

    // @SuppressWarnings("null")
    public Mono<yugiohCard> save(Mono<yugiohCard> card) {
        // return card.flatMap(
        // c -> {
        // if (c.getPrint() == null)
        // return Mono.just(c);
        // return innerPrintRepo.save(c.getPrint()).map(
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
        // return innerPrintRepo.findById(card.getPrintId()).map(
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
