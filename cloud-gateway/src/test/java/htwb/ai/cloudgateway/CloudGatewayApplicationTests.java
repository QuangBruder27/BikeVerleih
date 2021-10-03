package htwb.ai.cloudgateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CloudGatewayApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testWithoutTokenShould401() {
        WebTestClient client = WebTestClient.bindToApplicationContext(this.context)
                .build();
        client.get().uri("/bonus").exchange().expectStatus().isUnauthorized();
    }

    @Test
    public void testWithVaildTokenShouldRouted() {
        WebTestClient client = WebTestClient.bindToApplicationContext(this.context)
                .build();
        client.get().uri("/bonus").header("Authorization","default-token")
                .exchange()
                .expectStatus().is5xxServerError();
    }

}
