package htwb.ai.rentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.rentservice.Entity.Bike;
import htwb.ai.rentservice.Entity.Booking;
import htwb.ai.rentservice.Repo.BikeRepository;
import htwb.ai.rentservice.Repo.BookingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static htwb.ai.rentservice.Helper.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RentServiceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BikeRepository bikeRepository;

    // Request 1
    @Test
    void getAvailableBikeLocationTest200(){
        MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();
        parametersMap.add("latitude", "52.50823616494476");
        parametersMap.add("longtitude", "13.481563118096869");
        ResponseEntity<String> response = restTemplate.postForEntity("/rent/locatebike", parametersMap, String.class);
        assertTrue(response.getStatusCode().value() == 200);
    }

    // Request 1
    @Test
    void getAvailableBikeLocationTest401(){
        MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();
        parametersMap.add("latitude", "50.11002121010057");
        parametersMap.add("longtitude", "8.701847618905402");
        ResponseEntity<String> response = restTemplate.postForEntity("/rent/locatebike", parametersMap, String.class);
        assertTrue(response.getStatusCode().value() == 404);
    }

    // Request 2
    @Test
    void createBookingTest200(){
        Booking booking = initBooking();
        booking.setCustomerId("Tester1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId",booking.getCustomerId());
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(booking), headers);
        ResponseEntity<String> res = restTemplate.postForEntity("/rent/create", entity, String.class);
        System.out.println("Result: "+res);
        assertTrue(res.getStatusCodeValue()== 200);

        bookingRepository.deleteAll(bookingRepository.findBookingByCustomerId("Tester1"));
        Bike bike3 = bikeRepository.findById("B003").get();
        bike3.setStatus(BIKE_STATUS_AVAILABLE);
        bikeRepository.save(bike3);
    }

    // Request 2
    @Test
    void createBookingTest400IdMismatch(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId","Tester1");
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(initBooking()), headers);
        ResponseEntity<String> res = restTemplate.postForEntity("/rent/create", entity, String.class);
        System.out.println("Result: "+res);
        assertTrue(res.getStatusCodeValue()== 400);
    }

    // Request 2
    @Test
    void createBookingTest400NotAvailable(){
        Booking booking = initBooking();
        booking.setBikeId("B002");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId",booking.getCustomerId());
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(booking), headers);
        ResponseEntity<String> res = restTemplate.postForEntity("/rent/create", entity, String.class);
        System.out.println("Result: "+res);
        assertTrue(res.getStatusCodeValue()== 400);
    }

    // Request 2
    @Test
    void createBookingTest400PayloadError(){
        Booking booking = initBooking();
        booking.setBikeId("B999");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId",booking.getCustomerId());
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(booking), headers);
        ResponseEntity<String> res = restTemplate.postForEntity("/rent/create", entity, String.class);
        System.out.println("Result: "+res);
        assertTrue(res.getStatusCodeValue()== 400);
    }

    // Request 2
    @Test
    void createBookingTest406(){
        Booking booking = initBooking();
        booking.setCustomerId("KD0003");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId",booking.getCustomerId());
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(booking), headers);
        ResponseEntity<String> res = restTemplate.postForEntity("/rent/create", entity, String.class);
        System.out.println("Result: "+res);
        assertTrue(res.getStatusCodeValue()== 406);
    }

    // Request 3
    @Test
    void checkPinTest200(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "B002");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        String url = "/rent/pincheck?bikeId=B002&payloadPin=1111";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getStatusCode().value() == 200);
    }

    // Request 3
    @Test
    void checkPinTest400WrongPin(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "B002");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        String url = "/rent/pincheck?bikeId=B002&payloadPin=1234";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getBody().equals("Wrong pin"));
    }

    // Request 3
    @Test
    void checkPinTest400WrongNotReserved(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "B004");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        String url = "/rent/pincheck?bikeId=B004&payloadPin=1234";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getBody().equals("This bike is not yet reserved"));
    }

    // Request 3
    @Test
    void checkPinTest400BikeIdMismatch(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "B003");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        String url = "/rent/pincheck?bikeId=B004&payloadPin=1234";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getBody().equals("Bike Id mismatch"));
    }

    // Request 4
    @Test
    void updateBikeLocationTest200(){
        String url = "/rent/updatebikelocation?bikeId=B009&" +
                "latitude=52.45251458673761&longtitude=13.537500846336274";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "B003");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getStatusCode().value() == 200);
    }

    // Request 4
    @Test
    void updateBikeLocationTest400WrongId(){
        String url = "/rent/updatebikelocation?bikeId=B999&" +
                "latitude=52.45251458673761&longtitude=13.537500846336274";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "B999");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getBody().equals("Wrong bike id"));
    }

    // Request 5
    @Test
    void endRouteTest200(){
        Booking booking = initBooking();
        booking.setId(3);
        booking.setStatus(BKG_STATUS_COMPLETED);
        booking.setBikeId("B018");
        booking.setEndTime("2021-09-30 10:34:09");
        booking.setDistance(15);
        String url = "/rent/end";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "B018");
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(booking), headers);
        ResponseEntity<Booking> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Booking.class);
        System.out.println("Result: "+response);
        assertTrue(response.getStatusCode().value() == 200);
        Booking newBooking = response.getBody();
        newBooking.setStatus(BKG_STATUS_RESERVED);
        newBooking.setDistance(0);
        bookingRepository.save(newBooking);
    }

    // Request 5
    @Test
    void endRouteTest404(){
        Booking booking = initBooking();
        booking.setId(20);
        booking.setBikeId("B018");
        booking.setEndTime("2021-09-30 10:34:09");
        booking.setDistance(15);
        String url = "/rent/end";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "B018");
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(booking), headers);
        ResponseEntity<Booking> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Booking.class);
        System.out.println("Result: "+response);
        assertTrue(response.getStatusCode().value() == 404);
    }

    // Request 5
    @Test
    void endRouteTest400IdMismatch(){
        Booking booking = initBooking();
        booking.setId(6);
        booking.setBikeId("B018");
        booking.setEndTime("2021-09-30 10:34:09");
        booking.setDistance(15);
        String url = "/rent/end";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "B019");
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(booking), headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getBody().equals("Bike Id mismatch"));
    }

    // Request 6
    @Test
    void getAllBookingsByCustomerIdTest400CustomerIdMismatch(){
        String url = "/rent/history/KD0001";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "KD0002");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getBody().equals("Customer Id mismatch"));
    }

    // Request 6
    @Test
    void getAllBookingsByCustomerIdTest200(){
        String url = "/rent/history/KD0001";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "KD0001");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getStatusCode().value()==200);
    }

    // Request 6
    @Test
    void getAllBookingsByCustomerIdTest404(){
        String url = "/rent/history/Tester1";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "Tester1");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getStatusCode().value() == 404);
    }

    // Request 7
    @Test
    void getCurrentBookingByCustomerIdTest404(){
        String url = "/rent/now/KD0001";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "KD0001");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getStatusCode().value()==404);
    }

    // Request 7
    @Test
    void getCurrentBookingByCustomerIdTest200(){
        String url = "/rent/now/KD0002";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "KD0002");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getStatusCode().value()==200);
    }

    // Request 7
    @Test
    void getCurrentBookingByCustomerIdTest400IdMismatch(){
        String url = "/rent/now/KD0002";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "KD0003");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getBody().equals("Customer Id mismatch"));
    }




    Booking initBooking(){
        Booking booking = new Booking();
        booking.setCustomerId("Tester");
        booking.setBikeId("B003");
        return booking;
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
