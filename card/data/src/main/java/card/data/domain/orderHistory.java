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

    private @NonNull Long userId;//用户表主键

    @Transient
    private User user;//用户实体
    
    //未完成历史订单
    @Transient
    private List<cardOrder> unCompletedHistoryOrder = new ArrayList<>();
    private Set<Long> uncompletedHistoryOrderIds = new HashSet<>();

    //已完成历史订单
    @Transient 
    private List<cardOrder> completedHistoryOrder = new ArrayList<>();
    private Set<Long> completedHistoryOrderIds = new HashSet<>();

    public void setUser(User user) {
        this.user = user;
        if(user.getId() != null) userId = user.getId();
    }
}
