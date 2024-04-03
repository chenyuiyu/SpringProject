package card.data.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Table("OrderHistory")
public class orderHistory {
    
    @Id
    private Long id;

    private @NonNull String username;//用户表主键

    @Transient
    private User user;//用户实体
    
    //未完成历史订单
    @Transient
    private List<cardOrder> uncompletedHistoryOrder = new ArrayList<>();
    private Set<Long> uncompletedHistoryOrderIds = new HashSet<>();

    //已完成历史订单
    @Transient 
    private List<cardOrder> completedHistoryOrder = new ArrayList<>();
    private Set<Long> completedHistoryOrderIds = new HashSet<>();

    public void setUser(User user) {
        this.user = user;
        if(user.getUsername() != null) this.username = user.getUsername();
    }

    public void addUncompletedHistoryOrder(cardOrder order) {
        if(order != null) {
            this.uncompletedHistoryOrder.add(order);
            if(order.getId() != null) this.uncompletedHistoryOrderIds.add(order.getId());
        }
    }

    public void addUncompletedHistoryOrders(Iterable<cardOrder> orders) {
        for(cardOrder order : orders) this.addUncompletedHistoryOrder(order);
    }

    public void addCompletedHistoryOrder(cardOrder order) {
        if(order != null) {
            this.completedHistoryOrder.add(order);
            if(order.getId() != null) this.completedHistoryOrderIds.add(order.getId());
        }
    }

    public void addCompletedHistoryOrders(Iterable<cardOrder> orders) {
        for(cardOrder order : orders) this.addCompletedHistoryOrder(order);
    }
}
