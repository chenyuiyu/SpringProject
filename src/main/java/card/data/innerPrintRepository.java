package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import card.domain.innerPrint;

public interface innerPrintRepository extends ReactiveCrudRepository<innerPrint, Long>{
    
}
