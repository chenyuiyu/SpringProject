package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import card.domain.yugiohCard;

@Repository
public interface yugiohCardRepository extends ReactiveCrudRepository<yugiohCard, String>{
    
}
