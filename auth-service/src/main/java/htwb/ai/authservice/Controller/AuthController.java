package htwb.ai.authservice.Controller;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import htwb.ai.authservice.Entity.BikeAuth;
import htwb.ai.authservice.Entity.Customer;
import htwb.ai.authservice.Repo.BikeAuthRepository;
import htwb.ai.authservice.Repo.CustomerRepository;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="/auth")
public class AuthController {

    static String baseUrl = "http://localhost";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BikeAuthRepository bikeAuthRepository;

    /**
     * Request 1
     * Check the token
     * @param token
     * @return
     */
    @GetMapping("/check/{token}")
    public String checkToken(@PathVariable("token") String token) {
        System.out.println("GET checkToken by Auth Service");
        System.out.println(keyStore.toString());
        if (keyStore.containsKey(token)){
            return keyStore.get(token);
        } else {
            return "invalid";
        }
    }

    /**
     * Request 2
     * login for bike
     * @param bikeId
     * @param password
     * @return
     */
    @PostMapping("/bikelogin")
    public ResponseEntity getTokenForBike(@RequestParam String bikeId,
                                   @RequestParam String password) {
        if (bikeId.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Wrong format");
        }

        if (!bikeAuthRepository.existsById(bikeId)) {
            return ResponseEntity.status(401).body("Bike cannot be authenticated");
        } else {
            BikeAuth bikeAuth = bikeAuthRepository.findById(bikeId).get();
            if(bikeAuth.getPassword().equals(password)){
                return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(generateAuthTokenForBike(bikeAuth));
            } else {
                return ResponseEntity.status(401).body("Bike cannot be authenticated");
            }
        }
    }

    /**
     * Request 3
     * login for user
     * @param payloadCustomer
     * @return
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTokenForCustomer(@RequestBody Customer payloadCustomer) {
        System.out.println("payloadUser: "+payloadCustomer);
        if (payloadCustomer.getEmail().isEmpty() || payloadCustomer.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Wrong format");
        }

        if (!customerRepository.existsCustomByEmail(payloadCustomer.getEmail())) {
            System.out.println("Customer not found");
            return ResponseEntity.status(401).body("User cannot be authenticated");
        } else {
            Customer customer = customerRepository.findCustomByEmail(payloadCustomer.getEmail());
            if(customer.getPassword().equals(payloadCustomer.getPassword())){
                Map<String,String> map = new HashMap<>();
                map.put("token", generateAuthToken(customer));
                map.put("name",customer.getName());
                map.put("customerId",String.valueOf(customer.getCustomerId()));
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(map);
            } else {
                return ResponseEntity.status(401).body("User cannot be authenticated");
            }
        }
    }

    /**
     * Request 4
     * register the new account
     * @param payloadCustomer
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity createCustomerAccount(@RequestBody Customer payloadCustomer) {
        System.out.println("Create new account for customer");
        if (!payloadCustomer.isAcceptable() || payloadCustomer.getName()== null || payloadCustomer.getName().isEmpty()) {
            System.out.println("Wrong format");
            return ResponseEntity.badRequest().body("Wrong format");
        }

        if (customerRepository.existsCustomByEmail(payloadCustomer.getEmail())) {
            System.out.println("This email address is already in use.");
            return ResponseEntity.status(Response.SC_CONFLICT).body("This email address is already in use.");
        } else {
            System.out.println("Create customerId");
            payloadCustomer.setCustomerId(createCustomerId());
            System.out.println("payloadUser: "+payloadCustomer);
            Customer newCustomer = customerRepository.save(payloadCustomer);
            if(newCustomer != null){
                System.out.println("Create a new bonus account for customer");
                // Create bonus account for customer
                RestTemplate restTemplate = new RestTemplate();
                String url = baseUrl+ ":8200/bonus/create";
                MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();

                System.out.println("CUSTOMER ID = "+newCustomer.getCustomerId());
                parametersMap.add("customerId",String.valueOf(newCustomer.getCustomerId()));
                // don't call bonus service by testing
                if (!payloadCustomer.getEmail().equals("tester@gmail.com"))
                restTemplate.postForObject(url,parametersMap,String.class);

                Map<String,String> map = new HashMap<>();
                map.put("token", generateAuthToken(newCustomer));
                map.put("name",newCustomer.getName());
                map.put("customerId",String.valueOf(newCustomer.getCustomerId()));
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(map);

            } else {
                System.out.println("User cannot be authenticated");
                return ResponseEntity.status(Response.SC_UNAUTHORIZED).body("User cannot be authenticated");
            }
        }
    }

    /**
     * create a random number for id
     */
    public String createCustomerId(){
        String randomID = "KD"+ThreadLocalRandom.current().nextInt(1000, 9999 + 1);
        if (!customerRepository.existsById(randomID)){
            return  randomID;
        } else {
            return createCustomerId();
        }
    }

    public static BiMap<String, String> keyStore = HashBiMap.create();
    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateAuthToken(Customer customer) {
        if(!keyStore.containsKey(customer)) {
            keyStore.remove(customer);
        }
        String jws = Jwts.builder()
                .setSubject(customer.getCustomerId())
                //.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(10).toInstant()))
                .signWith(key).compact();
        keyStore.put(jws, customer.getCustomerId());
        return  jws;
    }

    public static String generateAuthTokenForBike(BikeAuth bikeAuth) {
        if(!keyStore.containsKey(bikeAuth)) {
            keyStore.remove(bikeAuth);
        }
        String jws = Jwts.builder()
                .setSubject(bikeAuth.getBikeId())
                //.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(10).toInstant()))
                .signWith(key).compact();
        keyStore.put(jws,bikeAuth.getBikeId());
        return  jws;
    }


}
