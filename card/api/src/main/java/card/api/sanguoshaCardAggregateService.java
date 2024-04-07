package card.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import card.data.printForShowRepository;
import card.data.sanguoshaCardRepository;
import card.data.skillRepository;
import card.domain.sanguoshaCard;
import card.domain.skill;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class sanguoshaCardAggregateService {
    @Autowired
    private printForShowRepository printForShowRepo;

    @Autowired
    private skillRepository skillRepo;
    
    @Autowired
    private sanguoshaCardRepository sanguoshaCardRepo;

    //@Transactional
    public Mono<sanguoshaCard> save(Mono<sanguoshaCard> card) {
        return card.flatMap(
            c -> {
                if(c.getPrint() == null) return Mono.just(c);
                return printForShowRepo.save(c.getPrint()).map(
                    p -> {
                        c.setPrint(p);
                        return c;
                    }
                );
            }
        ).flatMap(
            c -> {
                List<skill> skills = c.getSkills();
                if(skills.isEmpty()) return Mono.just(c);
                c.setSkills(new ArrayList<>());
                return skillRepo.saveAll(skills).map(
                    s -> {
                        c.addSkill(s);
                        return c;
                    }   
                ).last();
            }
        ).flatMap(sanguoshaCardRepo::save);
    }

    public Mono<sanguoshaCard> findById(String id) {
        if(id == null) return Mono.empty();
        return sanguoshaCardRepo.findById(id);
    }

    public Flux<sanguoshaCard> saveAll(Flux<sanguoshaCard> cards) {
        return cards.flatMap(
            card -> {
                return this.save(Mono.just(card));
            }
        );
    }
}

