package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import card.domain.printForShow;

@Repository
public interface printForShowRepository extends ReactiveCrudRepository<printForShow, String>{
    
}
