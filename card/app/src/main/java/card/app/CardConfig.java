package card.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!prod")
@Configuration
public class CardConfig {
    
    @Bean
    CommandLineRunner dataLoader() {
        return this::loadData; 
    }

    private void loadData(String... args) throws Exception {

    }
}
