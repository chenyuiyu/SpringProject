package card.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import card.data.dataLoader;
import card.data.sanguoshaCardRepository;

@SpringBootTest
public class dataLoaderTests {
    @Autowired
    private sanguoshaCardRepository sanguoshaCardRepo;

    @Test
    void testDataLoader() {
        sanguoshaCardRepo.count().subscribe(val -> assertEquals(val, 6));
    }
}
