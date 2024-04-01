package card.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import card.data.innerPrintRepository;
import card.data.sanguoshaCardRepository;
import card.data.skillRepository;
import card.data.domain.sanguoshaCard;
import card.data.domain.skill;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class sanguoshaCardAggregateService {
    @Autowired
    private innerPrintRepository innerPrintRepo;

    @Autowired
    private skillRepository skillRepo;
    
    @Autowired
    private sanguoshaCardRepository sanguoshaCardRepo;

    @SuppressWarnings("null")
    public Mono<sanguoshaCard> save(Mono<sanguoshaCard> card) {
        return card.flatMap(
            c -> {
                if(c.getPrint() == null) return Mono.just(c);
                return innerPrintRepo.save(c.getPrint()).map(
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

    @SuppressWarnings("null")
    public Mono<sanguoshaCard> findById(Long id) {
        if(id == null) return Mono.empty();
        return sanguoshaCardRepo.findById(id).flatMap(
            card -> {
                if(card.getPrintId() == null) return Mono.just(card);
                return innerPrintRepo.findById(card.getPrintId()).map(
                    p -> {
                        card.setPrint(p);
                        return card;
                    }
                );
            }
        ).flatMap(
            card -> {
                if(card.getSkillIds().isEmpty()) return Mono.just(card);
                return skillRepo.findAllById(card.getSkillIds()).map(
                    s -> {
                        card.addSkill(s);
                        return card;
                    }
                ).last();
            }
        );
    }

    public Flux<sanguoshaCard> saveAll(Flux<sanguoshaCard> cards) {
        return cards.flatMap(
            card -> {
                return this.save(Mono.just(card));
            }
        );
    }
}

