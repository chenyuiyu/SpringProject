package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import card.domain.orderHistory;
import reactor.core.publisher.Flux;

@Repository
public interface orderHistoryRepository extends ReactiveCrudRepository<orderHistory, String> {

    Flux<orderHistory> findByUsername(String username);
    
}
