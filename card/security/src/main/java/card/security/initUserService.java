package card.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import card.data.UserRepository;
import card.data.orderHistoryRepository;
import card.domain.User;
import card.domain.orderHistory;

@Service
public class initUserService {
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private orderHistoryRepository orderHistoryRepo;

    @Autowired
    private PasswordEncoder encoder;
    
    public void loadUser() {
        userRepo.save(new User("chenyuiyu", encoder.encode("88888888")))
        .flatMap(
            user -> {
                return orderHistoryRepo.save(new orderHistory(user.getUsername()));
            }
        ).subscribe();
    }
}
