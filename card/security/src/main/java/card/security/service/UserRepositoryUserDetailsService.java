package card.security.service;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import card.data.UserRepository;
import card.domain.User;
import reactor.core.publisher.Mono;

@Service
public class UserRepositoryUserDetailsService implements ReactiveUserDetailsService {

    private UserRepository userRepo;

    public UserRepositoryUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    
    @Override
    public Mono<UserDetails> findByUsername(String username) {

        return userRepo.findByUsername(username).map(user -> user.toUserDetails());
    }
    

    /* 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = userRepo.findByUsername(username).block();
        if(user != null) return user;
        throw new UnsupportedOperationException("User: '" + username + "' not found");
    }
    */
}
