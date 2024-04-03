package card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import card.data.dataLoader;
//import card.security.loadUser;

@SpringBootApplication
public class CardApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardApplication.class, args);
	}

    @Bean
    @Profile("!prod")
    CommandLineRunner loadData(dataLoader dataloader) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception{
                dataloader.loadData(args);
                //loaduser.initUsers();
            }
        };
    }

}
