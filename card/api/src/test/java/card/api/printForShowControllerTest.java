package card.api;

import static org.mockito.Mockito.when;

import java.io.IOException;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;
import java.nio.charset.StandardCharsets;

import card.domain.User;
import card.domain.printForShow;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class printForShowControllerTest {
    
    @Autowired
    private WebTestClient client;

    @SuppressWarnings({ "removal", "deprecation" })
    @Test
    public void shouldReturnAllprintForShows() throws IOException {

        //注意：json路径中的属性为小写
        client.get().uri("/printForShows")
        .header(
            "Authorization", 
            "Basic " + Base64Utils.encodeToString(
            "chenyuiyu:88888888".getBytes(StandardCharsets.UTF_8))
        )
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
    public void shouldAddprintForShow() {

        printForShow unSaved = new printForShow("?");

        client.post()
        .uri("/printForShows")
        .contentType(MediaType.APPLICATION_JSON)
        .body(unSaved, printForShow.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(printForShow.class)
        .isEqualTo(unSaved);
    }
}
