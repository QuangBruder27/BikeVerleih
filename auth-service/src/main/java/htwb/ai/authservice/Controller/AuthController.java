package htwb.ai.authservice.Controller;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import htwb.ai.authservice.Entity.BikeAuth;
import htwb.ai.authservice.Entity.Custom;
import htwb.ai.authservice.Repo.BikeAuthRepository;
import htwb.ai.authservice.Repo.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="/auth")
public class AuthController {

    static String baseUrl = "http://localhost";

    @Autowired
    private CustomRepository customRepository;

    @Autowired
    private BikeAuthRepository bikeAuthRepository;

    @GetMapping("/check/{token}")
    public String checkToken(@PathVariable("token") String token) {
        System.out.println("GET checkToken by Auth Service");

        //keyStore.put("default token","nguyen");

        System.out.println(keyStore.toString());
        if (keyStore.containsKey(token)){
            return keyStore.get(token);
        } else {
            return "invaild";
        }
    }

    @PostMapping("/get/bike")
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

    @PostMapping("/get")
    public ResponseEntity getTokenForCustom(@RequestParam String email,
                                   @RequestParam String password) {
        if (email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Wrong format");
        }

        if (!customRepository.existsCustomByEmail(email)) {
            return ResponseEntity.status(401).body("User cannot be authenticated");
        } else {
            Custom custom = customRepository.findCustomByEmail(email);
            if(custom.getPassword().equals(password)){
                return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(generateAuthToken(custom));
            } else {
                return ResponseEntity.status(401).body("User cannot be authenticated");
            }
        }
    }


    @PostMapping("/create")
    public ResponseEntity createCustomAccount(@RequestParam String email,
                                            @RequestParam String name,
                                            @RequestParam String password) {
        if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Wrong format");
        }

        if (customRepository.existsCustomByEmail(email)) {
            return ResponseEntity.status(401).body("This email address is already in use.");
        } else {
            Custom custom = new Custom();
            custom.setEmail(email);
            custom.setName(name);
            custom.setPassword(password);
            Custom newCustom = customRepository.save(custom);
            if(newCustom != null){
                // Create bonus account for custom
                RestTemplate restTemplate = new RestTemplate();
                String url = baseUrl+ ":8200/bonus";
                //Map<String,String> params = new HashMap<>();
                MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();

                System.out.println("CUSTOM ID = "+newCustom.getCustom_id());
                parametersMap.add("customId",String.valueOf(newCustom.getCustom_id()));
                restTemplate.postForObject(url,parametersMap,String.class);

                return ResponseEntity.status(201).body(newCustom);
            } else {
                return ResponseEntity.status(401).body("User cannot be authenticated");
            }
        }
    }

    public static BiMap<String, String> keyStore = HashBiMap.create();
    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateAuthToken(Custom custom) {
        if(!keyStore.containsKey(custom)) {
            keyStore.remove(custom);
        }
        String jws = Jwts.builder()
                .setSubject(custom.getEmail())
                //.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(10).toInstant()))
                .signWith(key).compact();
        keyStore.put(jws, custom.getEmail());
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
