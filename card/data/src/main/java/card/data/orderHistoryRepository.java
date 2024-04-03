package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import card.data.domain.orderHistory;
import reactor.core.publisher.Flux;

@Repository
public interface orderHistoryRepository extends ReactiveCrudRepository<orderHistory, Long> {

    Flux<orderHistory> findByUsername(String username);
    
}
