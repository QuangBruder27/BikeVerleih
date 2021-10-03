package htwb.ai.bonusservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.bonusservice.Entity.Bonus;
import htwb.ai.bonusservice.Repo.BonusRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BonusServiceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BonusRepository bonusRepository;

    // Resquest 1
    @Test
    void getBonusByCustomerIdTest200() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "KD0001");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = restTemplate.exchange("/bonus/KD0001", HttpMethod.GET, entity, String.class);
        assertTrue(response.getStatusCode().value() == 200);
    }

    // Resquest 1
    @Test
    void getBonusByCustomerIdTest400() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "KD1");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = restTemplate.exchange("/bonus/KD0001", HttpMethod.GET, entity, String.class);
        assertTrue(response.getStatusCode().value() == 400);
    }

    // Request 2
    @Test
    void addBonusScoreByRentTest200() {
        Map<String, String> headerParameters = new HashMap<>();
        headerParameters.put("currentId", "KD0001");
        ResponseEntity response = restTemplate.exchange("/bonus/plus/KD0001/10", HttpMethod.PUT, null, String.class, headerParameters);
        System.out.println("Response: " + response);
        assertTrue(response.getStatusCode().value() == 200);
    }

    // Request 2
    @Test
    void addBonusScoreByRentTest400() {
        ResponseEntity<String> response = restTemplate.exchange("/bonus/plus/KD1/10", HttpMethod.PUT, null, String.class);
        System.out.println("Response: " + response);
        assertTrue(response.getStatusCode().value() == 400);
    }

    // Request 3
    @Test
    void createBonusAccountTest200() {
        MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();
        parametersMap.add("customerId", "KD9999");
        ResponseEntity<String> response = restTemplate.postForEntity("/bonus/create", parametersMap, String.class);
        if (bonusRepository.existsById("KD9999")) bonusRepository.deleteById("KD9999");
        assertTrue(response.getStatusCode().value() == 200);
    }

    // Request 3
    @Test
    void createBonusAccountTest400() {
        //ResponseEntity<String> response = restTemplate.exchange("/bonus/KD0001", HttpMethod.POST,entity,String.class);
        MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();
        parametersMap.add("customerId", "KD0001");
        ResponseEntity<String> response = restTemplate.postForEntity("/bonus/create", parametersMap, String.class);
        assertTrue(response.getStatusCode().value() == 400);
    }

    // Request 4
    @Test
    void addBonusScoreByReportTest200() {
        ResponseEntity<String> response = restTemplate.exchange("/bonus/add/KD0001/10", HttpMethod.PUT, null, String.class);
        System.out.println("Response: " + response);
        assertTrue(response.getStatusCode().value() == 200);
    }

    // Request 4
    @Test
    void addBonusScoreByReportTest400() {
        ResponseEntity<String> response = restTemplate.exchange("/bonus/add/KD9999/10", HttpMethod.PUT, null, String.class);
        System.out.println("Response: " + response);
        assertTrue(response.getStatusCode().value() == 400);
    }





}
