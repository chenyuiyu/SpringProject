package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import card.data.domain.yugiohCard;


public interface yugiohCardRepository extends ReactiveCrudRepository<yugiohCard, Long>{
    
}
