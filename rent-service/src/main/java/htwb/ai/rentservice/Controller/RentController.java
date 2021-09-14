package htwb.ai.rentservice.Controller;

import com.google.common.collect.Lists;
import htwb.ai.rentservice.Entity.Bike;
import htwb.ai.rentservice.Entity.Booking;
import htwb.ai.rentservice.Repo.BikeRepository;
import htwb.ai.rentservice.Repo.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import android.location.Location;

import static htwb.ai.rentservice.Helper.*;

@RestController
@RequestMapping("/rent")
public class RentController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BikeRepository bikeRepository;

    @PostMapping("/locatebike")
    public ResponseEntity getBikeLocation(@RequestParam String latitude,
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


    @PostMapping("/create")
    public ResponseEntity createBooking(@RequestHeader String Authorization,@RequestBody Booking payloadBooking){
        System.out.println("Header: "+Authorization);

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
                return ResponseEntity.badRequest().body("You can only reserve one bike ");
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
            Booking newBooking = bookingRepository.save(booking);
            return ResponseEntity.ok().contentType( MediaType.TEXT_PLAIN).body(String.valueOf(randomNum));
        } else {
            System.out.println("Failure");
            return ResponseEntity.badRequest().body("Failure");
        }
    }

    @GetMapping("/check")
    public ResponseEntity checkPin(@RequestParam String bikeId,
                                       @RequestParam Integer pin){
        System.out.println("GET SERVICE: Ceck pin for bike id: "+ bikeId+",with pin: "+pin);
        if(!bikeRepository.existsById(bikeId)){
            return  ResponseEntity.notFound().build();
        }
        Bike bike = bikeRepository.findById(bikeId).get();
        if (!bike.getStatus().equals(BIKE_STATUS_RESERVED)){
            return ResponseEntity.badRequest().body("This bike is not yet reserved ");
        }
        if (bike.getPin().equals(pin)){
            return  ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.badRequest().body("Wrong pin");
        }
    }

    @PutMapping("/start")
    public ResponseEntity startRoute(@RequestParam String bikeId,
                                     @RequestParam Integer bookingId,
                                     @RequestParam String beginTime){
        System.out.println("PUT SERVICE:Start the route: "+ bookingId+",with bike: "+bikeId);
        if(beginTime.isEmpty() || bikeId.isEmpty() || bookingId==null
                || !bookingRepository.existsById(bookingId) || !bikeRepository.existsById(bikeId)){
            return  ResponseEntity.notFound().build();
        }
        Booking booking =bookingRepository.findById(bookingId).get();
        if (!booking.getBikeId().equals(bikeId)){
            return ResponseEntity.badRequest().body("Wrong bike id");
        }

        booking.setStatus(BKG_STATUS_RUNNING);
        booking.setBeginTime(beginTime);
        Booking newBooking = bookingRepository.save(booking);

        if (newBooking != null){
            return  ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Failure");
        }
    }

    @PutMapping("/end")
    public ResponseEntity endRoute(@RequestParam String bikeId,
                                   @RequestParam Integer bookingId,
                                   @RequestParam String endTime,
                                   @RequestParam Integer distance){
        System.out.println("PUT SERVICE:Start the route: "+ bookingId+",with bike: "+bikeId);
        if(distance==null ||endTime.isEmpty() || bikeId.isEmpty() || bookingId==null
                || !bookingRepository.existsById(bookingId) || !bikeRepository.existsById(bikeId)){
            return  ResponseEntity.notFound().build();
        }
        Booking booking =bookingRepository.findById(bookingId).get();
        if (!booking.getBikeId().equals(bikeId)){
            return ResponseEntity.badRequest().body("Wrong bike id");
        }

        booking.setStatus(BKG_STATUS_COMPLETED);
        booking.setEndTime(endTime);
        booking.setDistance(distance);
        Booking newBooking = bookingRepository.save(booking);

        Bike bike = bikeRepository.findById(bikeId).get();
        bike.setStatus(BIKE_STATUS_AVAILABLE);
        bike.setPin(null);
        Bike newBike = bikeRepository.save(bike);

        if (newBooking != null && newBike!= null){
            return  ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Failure");
        }
    }

    @GetMapping("/history/{customerId}")
    public ResponseEntity getAllBookingByCustomer(@RequestHeader String Authorization,@PathVariable(value = "customerId") String customerId){
        System.out.println("Header: "+Authorization);
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

    @GetMapping("/now/{customerid}")
    public ResponseEntity getCurrentBookingById(@RequestHeader String Authorization,@PathVariable(value = "customerid") String customerId){
        System.out.println("Header: "+Authorization);
        System.out.println("GET current booking by customerId: "+ customerId);
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


}
