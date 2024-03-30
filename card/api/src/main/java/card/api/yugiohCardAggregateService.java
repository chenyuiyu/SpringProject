package card.api;

import org.springframework.stereotype.Service;

import card.data.innerPrintRepository;
import card.data.yugiohCardRepository;
import card.data.domain.yugiohCard;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class yugiohCardAggregateService {

    private yugiohCardRepository yugiohCardRepo;
    private innerPrintRepository innerPrintRepo;

    public Mono<yugiohCard> save(Mono<yugiohCard> card) {
        //TODO
        return card;
    }

    public Mono<yugiohCard> findById(Long id) {
        //TODO
        return Mono.empty();
    }

    public Flux<yugiohCard> saveAll(Flux<yugiohCard> cards) {
        return Flux.empty();
    }
}

