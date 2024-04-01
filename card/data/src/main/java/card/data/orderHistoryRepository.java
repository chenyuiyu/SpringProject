package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import card.data.domain.orderHistory;
import reactor.core.publisher.Flux;


public interface orderHistoryRepository extends ReactiveCrudRepository<orderHistory, Long> {

    Flux<orderHistory> findByUserId(Long userId);
    
}
