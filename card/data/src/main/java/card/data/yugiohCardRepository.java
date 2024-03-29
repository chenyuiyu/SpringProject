package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import card.domain.yugiohCard;

public interface yugiohCardRepository extends ReactiveCrudRepository<yugiohCard, Long>{
    
}
