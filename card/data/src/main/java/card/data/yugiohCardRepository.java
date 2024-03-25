package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import card.domain.yugiohCard;

public interface yugiohCardRepository extends ReactiveCrudRepository<yugiohCard, String>{
    //TODO: 如果需要额外的sql需求，请在下面定义
}
