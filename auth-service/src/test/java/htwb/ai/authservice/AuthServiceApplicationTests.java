package htwb.ai.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.authservice.Entity.Customer;
import htwb.ai.authservice.Repo.CustomerRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthServiceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    // request 1
    @Test
    void checkTokenTestFalse(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.getForEntity("http://localhost:" + port+ "/auth/check/ abcderf",String.class);
        System.out.println("result: "+result.getBody());
        assertTrue(result.getBody().equals("invalid"));
    }

    // request 1
    @Test
    void checkTokenTestTrue(){
        MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();
        parametersMap.add("bikeId", "B001");
        parametersMap.add("password", "1357");
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/bikelogin", parametersMap, String.class);
        String token = response.getBody();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.getForEntity("http://localhost:" + port+ "/auth/check/"+token,String.class);
        assertTrue(!result.getBody().equals("invalid"));
    }

    // Request 2
    @Test
    void getTokenForBikeTest200(){
        MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();
        parametersMap.add("bikeId", "B001");
        parametersMap.add("password", "1357");
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/bikelogin", parametersMap, String.class);
        System.out.println("Result: "+response.getBody());
        assertTrue(response.getStatusCode().value() == 200);
    }

    // Request 2
    @Test
    void getTokenForBikeTest401(){
        MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();
        parametersMap.add("bikeId", "B001");
        parametersMap.add("password", "13579");
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/bikelogin", parametersMap, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getStatusCode().value() == 401);
    }



    // Request 3
    @Test
    void getTokenForCustomerTest401(){
        Customer customer = new Customer();
        customer.setEmail("email1@gmail.com");
        customer.setPassword("1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(customer), headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getStatusCode().value() == 401);
    }

    // Request 3
    @Test
    void getTokenForCustomerTest200(){
        Customer customer = new Customer();
        customer.setEmail("email1@gmail.com");
        customer.setPassword("6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(customer), headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", entity, String.class);
        assertTrue(response.getStatusCode().value() == 200);
    }

    // Request 4
    @Test
    void createCustomerAccountTest200(){
        Customer customer = new Customer();
        customer.setEmail("tester@gmail.com");
        customer.setPassword("123");
        customer.setName("Tester");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(customer), headers);
        ResponseEntity<Customer> response = restTemplate.postForEntity("/auth/register", entity, Customer.class);
        System.out.println("Result: "+response);
        if(customerRepository.existsById(response.getBody().getCustomerId()))
            customerRepository.deleteById(response.getBody().getCustomerId());
        assertTrue(response.getStatusCode().value() == 200);
    }

    // Request 4
    @Test
    void createCustomerAccountTest409(){
        Customer customer = new Customer();
        customer.setEmail("email1@gmail.com");
        customer.setPassword("123");
        customer.setName("Tester");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(customer), headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/register", entity, String.class);
        System.out.println("Result: "+response);
        System.out.println("Body:"+response.getBody());
        assertTrue(response.getStatusCode().value() == 409);
    }

    // Request 4
    @Test
    void createCustomerAccountTest400(){
        Customer customer = new Customer();
        customer.setEmail("newemail@gmail.com");
        customer.setPassword("");
        customer.setName("");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(customer), headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/register", entity, String.class);
        System.out.println("Result: "+response);
        System.out.println("Body:"+response.getBody());
        assertTrue(response.getStatusCode().value() == 400);
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
