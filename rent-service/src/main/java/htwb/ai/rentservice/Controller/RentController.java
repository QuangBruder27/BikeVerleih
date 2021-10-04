package htwb.ai.rentservice.Controller;

import com.google.common.collect.Lists;
import htwb.ai.rentservice.Entity.Bike;
import htwb.ai.rentservice.Entity.Booking;
import htwb.ai.rentservice.Repo.BikeRepository;
import htwb.ai.rentservice.Repo.BookingRepository;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import android.location.Location;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.HeaderParam;

import static htwb.ai.rentservice.Helper.*;

@RestController
@RequestMapping("/rent")
public class RentController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BikeRepository bikeRepository;


    public double calculateDist(String oriLatitude,String oriLongtitude, String destLatitude,String destLongtide){
        Location oriLocation = new Location("LocationManager.GPS_PROVIDER");
        oriLocation.setLatitude(Double.valueOf(oriLatitude));
        oriLocation.setLongitude(Double.valueOf(oriLongtitude));
        Location destLocation = new Location("LocationManager.GPS_PROVIDER");
        destLocation.setLatitude(Double.valueOf(destLatitude));
        destLocation.setLongitude(Double.valueOf(destLongtide));
        return calculateDistance(oriLocation,destLocation);
    }

    public double calculateDistance(Location origin, Location destination){
        if (origin!= null && destination!=null) {
            return  (double) Math.round(origin.distanceTo(destination)/1000.0 * 100) / 100;
        }
        return 99.99;
    }

    /*
    @GetMapping("/{id}")
    public ResponseEntity getBookingById(@PathVariable(value = "id") Integer id){
        System.out.println("GET booking by id: "+ id);
       Booking booking = bookingRepository.findById(id).get();
        System.out.println("Booking: "+booking);
        if (booking != null){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(booking.toString());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

     */

    // Request 1
    @PostMapping("/locatebike")
    public ResponseEntity getAvailableBikeLocation(@RequestParam String latitude,
                                                   @RequestParam String longtitude){
        System.out.println("GET bike location for user with gps:"+ latitude+", "+longtitude);
        List<Bike> listOfAllBikes = Lists.newArrayList(bikeRepository.findAll());
        for (Bike bike: listOfAllBikes){
            System.out.println("Bike: "+bike);
        }
        List<Bike> listOfBike = listOfAllBikes.stream().filter(x -> x.getStatus().equals("available"))
                .filter(x -> calculateDist(x.getLatitude(),x.getLongtitude(),latitude,longtitude)<5.0)
                .collect(Collectors.toList());
        if (listOfBike.size()>1){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listOfBike);
        } else {
            return  ResponseEntity.notFound().build();
        }
    }
    // Request 2
    @PostMapping("/create")
    public ResponseEntity createBooking(@RequestHeader String currentId,
                                        @RequestBody Booking payloadBooking){
        System.out.println("Header: "+currentId);
        if(!currentId.equals(payloadBooking.getCustomerId()))
            return ResponseEntity.badRequest().body("Customer Id mismatch");

        String bikeId = payloadBooking.getBikeId();
        String customerId = payloadBooking.getCustomerId();
        System.out.println("Post rent-creat booking: bike:"+bikeId+",custom:"+customerId);
        if (bikeId.isEmpty() || customerId.isEmpty()){
            System.out.println("Failure. Empty payload");
            return ResponseEntity.badRequest().body("Failure. Empty payload");
        }
        if(!bikeRepository.existsById(bikeId) ){
            System.out.println("Bike id is wrong");
            return ResponseEntity.badRequest().body("Bike id is wrong");
        }
        Bike bike = bikeRepository.findById(bikeId).get();
        if (!bike.getStatus().equals(BIKE_STATUS_AVAILABLE)){
            System.out.println("This bike is not for rent right now");
            return ResponseEntity.badRequest().body("This bike is not for rent right now");
        }

        if (bookingRepository.existsBookingByCustomerId(customerId)){
            List<Booking> listOfOldBooking = bookingRepository.findBookingByCustomerId(customerId);
            List<Booking> actualBooking = listOfOldBooking.stream()
                    .filter(x-> !(x.getStatus().equals(BKG_STATUS_COMPLETED))).collect(Collectors.toList());
            if (actualBooking.size()>0){
                System.out.println("You can only reserve one bike");
                return ResponseEntity.status(Response.SC_NOT_ACCEPTABLE).body("You can only reserve one bike ");
            }
        }

        if (bike.getStatus().equals(BIKE_STATUS_AVAILABLE)){
            // create random pin for bike
            int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999 + 1);
            bike.setPin(randomNum);
            System.out.println("PIN = "+randomNum);
            bike.setStatus(BIKE_STATUS_RESERVED);
            bikeRepository.save(bike);

            // create booking
            Booking booking = new Booking(customerId,bikeId,BKG_STATUS_RESERVED);
            booking.setBeginTime(simpleDateFormat.format(new Timestamp(System.currentTimeMillis())));
            Booking newBooking = bookingRepository.save(booking);
            return ResponseEntity.ok().contentType( MediaType.TEXT_PLAIN).body(String.valueOf(randomNum));
        } else {
            System.out.println("Failure");
            return ResponseEntity.badRequest().body("Failure");
        }
    }

    // Request 3
    @GetMapping("/pincheck")
    public ResponseEntity checkPin(@RequestHeader String currentId,
                                   @RequestParam String bikeId,
                                   @RequestParam String payloadPin){
        System.out.println("GET SERVICE: Check pin for bike id: "+ bikeId+",with pin: "+payloadPin);
        if(!bikeRepository.existsById(bikeId)){
            return  ResponseEntity.notFound().build();
        }
        if (!currentId.equals(bikeId)) return ResponseEntity.badRequest().body("Bike Id mismatch");
        Bike bike = bikeRepository.findById(bikeId).get();
        if (!bike.getStatus().equals(BIKE_STATUS_RESERVED)){
            return ResponseEntity.badRequest().body("This bike is not yet reserved");
        }
        if (bike.getPin().toString().equals(payloadPin)){
            Booking booking = bookingRepository.findBookingByBikeId(bike.getBikeId())
                    .stream().filter(x-> x.getStatus().equals("reserved")).collect(Collectors.toList()).get(0);
            if (booking !=null){
                return  ResponseEntity.ok(booking.getId().toString());
            } else {
                return ResponseEntity.badRequest().body("Failure ");
            }

        } else {
            return ResponseEntity.badRequest().body("Wrong pin");
        }
    }

    // Request 4
    @PutMapping("/updatebikelocation")
    public ResponseEntity updateBikeLocation(@HeaderParam("currentId") String currentId,
                                             @RequestParam String bikeId,
                                             @RequestParam String latitude,
                                             @RequestParam String longtitude){
        if (null != currentId)  return ResponseEntity.status(Response.SC_METHOD_NOT_ALLOWED).build();

        System.out.println("Update Bike Location: "+ latitude+","+longtitude);
        if(latitude.isEmpty() || longtitude.isEmpty() || bikeId.isEmpty()){
            return ResponseEntity.badRequest().body("Failure");
        }
        if (!bikeRepository.existsById(bikeId)){
            return ResponseEntity.badRequest().body("Wrong bike id");
        }
        Bike bike = bikeRepository.findById(bikeId).get();
        bike.setLatitude(latitude);
        bike.setLongtitude(longtitude);
        Bike newBike = bikeRepository.save(bike);

        if (newBike != null){
            return  ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Failure");
        }
    }

    // Request 5
    @PutMapping("/end")
    public ResponseEntity endRoute(@RequestHeader String currentId,
                                   @RequestBody Booking payloadBooking){
        System.out.println("PUT SERVICE:End the route: "+ payloadBooking);
        System.out.println();
        if(!payloadBooking.isAcceptable()
                || !bookingRepository.existsById(payloadBooking.getId())
                || !bikeRepository.existsById(payloadBooking.getBikeId())){
            return  ResponseEntity.notFound().build();
        }

        if(!currentId.equals(payloadBooking.getBikeId()))
            return ResponseEntity.badRequest().body("Bike Id mismatch");

        Booking booking = bookingRepository.findById(payloadBooking.getId()).get();
        System.out.println("Booking from findById: "+booking);

        booking.setStatus(BKG_STATUS_COMPLETED);
        booking.setEndTime(payloadBooking.getEndTime());
        booking.setDistance(Integer.valueOf(payloadBooking.getDistance()));
        System.out.println("Booking Status: "+booking.getStatus());

        Booking newBooking = bookingRepository.save(booking);
        System.out.println("newBooking Status: "+newBooking.getStatus());

        Bike bike = bikeRepository.findById(payloadBooking.getBikeId()).get();
        bike.setStatus(BIKE_STATUS_AVAILABLE);
        bike.setPin(0);
        Bike newBike = bikeRepository.save(bike);

        if (newBooking != null && newBike!= null){
            System.out.println("CustomerId for Error: "+newBooking.getCustomerId());
            if (!newBooking.getCustomerId().equals("Tester"))
                addBonusScore(newBooking.getCustomerId(), newBooking.getDistance());
            return ResponseEntity.ok(newBooking);

        } else {
            return ResponseEntity.badRequest().body("Failure");
        }
    }

    // Request 6
    @GetMapping("/history/{customerId}")
    public ResponseEntity getAllBookingsByCustomerId(@RequestHeader String currentId,
                                                   @PathVariable(value = "customerId") String customerId){
        if(!currentId.equals(customerId))
            return ResponseEntity.badRequest().body("Customer Id mismatch");

        System.out.println("GET all booking by customerId: "+ customerId);
        List<Booking> list =  bookingRepository.findBookingByCustomerId(customerId);
                //.stream().filter(x-> x.getStatus().equals("completed")).collect(Collectors.toList());
        System.out.println("Booking: "+list);
        if (list.size()>0){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Request 7
    @GetMapping("/now/{customerid}")
    public ResponseEntity getCurrentBookingByCustomerId(@RequestHeader String currentId,
                                                @PathVariable(value = "customerid") String customerId){
        System.out.println("GET current booking by customerId: "+ customerId);
        if(!currentId.equals(customerId)){
            System.out.println("Customer Id mismatch: "+currentId);
            return ResponseEntity.badRequest().body("Customer Id mismatch");
        }
        List<Booking> list =  bookingRepository.findBookingByCustomerId(customerId)
                                .stream().filter(x->
                                x.getStatus().equals("reserved") ||
                                        x.getStatus().equals("running")
                        ).collect(Collectors.toList());
        System.out.println("Booking: "+list);
        if (!list.isEmpty()){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list.get(0));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public void addBonusScore(String customerId, Integer distance){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8100/bonus?" +
                "customerId="+customerId+"&"+
                "payloadDistance="+distance;
        restTemplate.put(url,null);
    }





}
