package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import card.domain.skill;

@Repository
public interface skillRepository extends ReactiveCrudRepository<skill, String> {
    
}
