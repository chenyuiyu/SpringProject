package card.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import card.data.dataLoader;

@Profile("!prod")
@Configuration
public class CardConfig {

    private dataLoader dataloader;

    public CardConfig(dataLoader dataloader) {
        this.dataloader = dataloader;
    }

    @Bean
    CommandLineRunner loadData() {
        return dataloader::loadData; 
    }

}
