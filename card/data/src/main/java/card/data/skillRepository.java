package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import card.data.domain.skill;


public interface skillRepository extends ReactiveCrudRepository<skill, Long> {
    
}
