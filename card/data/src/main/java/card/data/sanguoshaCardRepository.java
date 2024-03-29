package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import card.domain.sanguoshaCard;

public interface sanguoshaCardRepository extends ReactiveCrudRepository<sanguoshaCard, Long> {
    
} 
