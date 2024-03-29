package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import card.domain.skill;

public interface skillRepository extends ReactiveCrudRepository<skill, Long> {
    
}
