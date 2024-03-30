package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import card.data.domain.sanguoshaCard;

@Repository
public interface sanguoshaCardRepository extends ReactiveCrudRepository<sanguoshaCard, Long> {
    
} 
