package card.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import card.data.UserRepository;
import card.data.domain.User;

@Service
public class loadUser {
    
    private PasswordEncoder encoder;
    private UserRepository userRepo;

    public loadUser(PasswordEncoder encoder, UserRepository userRepo) {
        this.encoder = encoder;
        this.userRepo = userRepo;
    }

    public void initUsers() {
        //加入初始用户
        userRepo.save(new User("chenyuiyu", encoder.encode("88888888"))).subscribe();
    }
}
