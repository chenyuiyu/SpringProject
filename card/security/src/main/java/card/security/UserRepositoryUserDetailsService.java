package card.security;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import card.data.UserRepository;
import card.data.domain.User;
import reactor.core.publisher.Mono;

@Service
public class UserRepositoryUserDetailsService implements ReactiveUserDetailsService {

    private UserRepository userRepo;

    public UserRepositoryUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        
        return userRepo.findByUsername(username).map(user -> (UserDetails)user);
    }
}
