package htwb.ai.locationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.locationservice.Entity.Location;
import htwb.ai.locationservice.Repo.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LocationServiceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void insertLocationTest200() {
        String url = "/locate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "Tester");
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(initLocation()), headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getStatusCode().value()==200);
    }

    @Test
    void insertLocationTest400IdMismatch() {
        String url = "/locate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "Tester1");
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(initLocation()), headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getBody().equals("Bike Id mismatch"));
    }


    Location initLocation(){
        return new Location("Tester","52.51351291681828","13.48216341414339","2021-09-29 23:30:09");
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
