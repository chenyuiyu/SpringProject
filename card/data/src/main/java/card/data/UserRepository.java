package card.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import card.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import card.domain.User;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {

    Mono<User> findByUsername(String username);

    Mono<User> findByEmail(String email);  
    
    default Flux<User> findByAuthority(GrantedAuthority authority) {
        return this.findAll().filter(
            user -> {
                return user.getAuthorities().contains(authority);
            }
        );
    };
} 
