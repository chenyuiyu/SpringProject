package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import card.domain.cardOrder;

@Repository
public interface cardOrderRepository extends ReactiveCrudRepository<cardOrder, String> {

    
}
