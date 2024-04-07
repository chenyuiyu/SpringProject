package card.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import card.data.cardOrderRepository;
import card.domain.cardOrder;
import card.domain.sanguoshaCard;
import card.domain.yugiohCard;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class cardOrderAggregateService {
    
    private cardOrderRepository cardOrderRepo;
    private sanguoshaCardAggregateService sanguoshaCardAggregator;
    private yugiohCardAggregateService yugiohCardAggregator;
    
    //@Transactional
    public Mono<cardOrder> save(Mono<cardOrder> order) {
        if(order == null || Mono.empty().equals(order)) return Mono.empty();
        return order.flatMap(
            cur -> {
                List<sanguoshaCard> cards = cur.getSanguoshaCards();
                if(cards.isEmpty()) return Mono.just(cur);
                cur.setSanguoshaCards(new ArrayList<>());
                return sanguoshaCardAggregator.saveAll(Flux.fromIterable(cards))
                .doOnNext(card -> cur.addSanGuoShaCard(card))
                .map(card -> cur)
                .last();
            }
        ).flatMap(
            cur -> {
                List<yugiohCard> cards = cur.getYugiohCards();
                if(cards.isEmpty()) return Mono.just(cur);
                cur.setYugiohCards(new ArrayList<>());
                return yugiohCardAggregator.saveAll(Flux.fromIterable(cards))
                .doOnNext(card -> cur.addYuGiOhCard(card))
                .map(card -> cur)
                .last();
            }
        ).flatMap(cardOrderRepo::save);
    }

    public Mono<cardOrder> findById(String id) {
        if(id == null) return Mono.empty();
        return cardOrderRepo.findById(id);
    }

    public Flux<cardOrder> saveAll(Flux<cardOrder> orders) {
        return orders.flatMap(
            order -> {
                return this.save(Mono.just(order));
            }
        );
    }
}
