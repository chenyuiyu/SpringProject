package card.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import card.data.dataLoader;

@SpringBootApplication
@ComponentScan(basePackages = {"card.data"})
public class CardApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardApplication.class, args);
	}

    @Bean
    @Profile("!prod")
    CommandLineRunner loadData(@Autowired dataLoader dataloader) {
        return dataloader::loadData; 
    }

}
