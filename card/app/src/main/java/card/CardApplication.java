package card;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.config.EnableWebFlux;

import card.data.dataLoader;
import card.security.service.initUserService;

@SpringBootApplication(exclude = {WebMvcAutoConfiguration.class})
@EnableWebFlux
public class CardApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardApplication.class, args);
	}

    @Bean
    @Profile("!prod")
    CommandLineRunner initData(dataLoader dataloader, initUserService userLoader) {
        
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                dataloader.loadData(args);
                userLoader.loadUser();
            }
        };
    }

}
