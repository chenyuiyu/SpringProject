package card.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import card.data.cardOrderRepository;
import card.data.domain.cardOrder;
import card.data.domain.sanguoshaCard;
import card.data.domain.yugiohCard;
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

    public Mono<cardOrder> findById(Long id) {
        if(id == null) return Mono.empty();
        return cardOrderRepo.findById(id).flatMap(
            order -> {
                Set<Long> cards = order.getSanguoshaCardIds();
                if(cards.isEmpty()) return Mono.just(order);
                return Flux.fromIterable(cards)
                .flatMap(x -> sanguoshaCardAggregator.findById(x))
                .doOnNext(card -> order.addSanGuoShaCard(card))
                .map(card -> order)
                .last();
            }
        ).flatMap(
            order -> {
                Set<Long> cards = order.getYugiohCardIds();
                if(cards.isEmpty()) return Mono.just(order);
                return Flux.fromIterable(cards)
                .flatMap(x -> yugiohCardAggregator.findById(x))
                .doOnNext(card -> order.addYuGiOhCard(card))
                .map(card -> order)
                .last();
            }
        );
    }

    public Flux<cardOrder> saveAll(Flux<cardOrder> orders) {
        return orders.flatMap(
            order -> {
                return this.save(Mono.just(order));
            }
        );
    }
}
