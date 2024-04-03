package card.api;

import static org.mockito.Mockito.when;

import java.io.IOException;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import card.data.domain.User;
import card.data.domain.innerPrint;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class InnerPrintControllerTest {
    
    @Autowired
    private WebTestClient client;

    @Test
    public void shouldReturnAllInnerPrints() throws IOException {

        //注意：json路径中的属性为小写
        client.get().uri("/innerPrints")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$").isArray()
        .jsonPath("$.length()").isEqualTo(6)
        .jsonPath(String.format("$[?@.url == %s]", TestConfig.sanguoxiuUrl)).exists()
        .jsonPath(String.format("$[?@.url == %s]", TestConfig.qunUrl)).exists()
        .jsonPath("$[0]").exists()
        .jsonPath("$[1]").exists()
        .jsonPath("$[2]").exists()
        .jsonPath("$[3]").exists()
        .jsonPath("$[4]").exists()
        .jsonPath("$[5]").exists()
        .jsonPath("$[6]").doesNotExist();
    }

    @Test
    public void shouldAddInnerPrint() {

        innerPrint unSaved = new innerPrint("?");

        client.post()
        .uri("/innerPrints")
        .contentType(MediaType.APPLICATION_JSON)
        .body(unSaved, innerPrint.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(innerPrint.class)
        .isEqualTo(unSaved);
    }
}
