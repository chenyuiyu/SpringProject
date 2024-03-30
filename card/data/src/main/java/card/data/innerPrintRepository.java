package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import card.data.domain.innerPrint;


public interface innerPrintRepository extends ReactiveCrudRepository<innerPrint, Long>{
    
}
