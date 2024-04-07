package card.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Document
public class orderHistory {
    
    @Id
    private String id;

    private @NonNull String username;//用户表主键
    
    //未完成历史订单
    private List<cardOrder> uncompletedHistoryOrder = new ArrayList<>();

    //已完成历史订单
    private List<cardOrder> completedHistoryOrder = new ArrayList<>();

    public void addUncompletedHistoryOrder(cardOrder order) {
        if(order != null) {
            this.uncompletedHistoryOrder.add(order);
        }
    }

    public void addUncompletedHistoryOrders(Iterable<cardOrder> orders) {
        for(cardOrder order : orders) this.addUncompletedHistoryOrder(order);
    }

    public void addCompletedHistoryOrder(cardOrder order) {
        if(order != null) {
            this.completedHistoryOrder.add(order);
        }
    }

    public void addCompletedHistoryOrders(Iterable<cardOrder> orders) {
        for(cardOrder order : orders) this.addCompletedHistoryOrder(order);
    }
}
