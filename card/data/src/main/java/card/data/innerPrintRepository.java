package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import card.domain.innerPrint;

public interface innerPrintRepository extends ReactiveCrudRepository<innerPrint, Long>{
    //TODO: 如果需要额外的sql需求，请在下面定义
}
