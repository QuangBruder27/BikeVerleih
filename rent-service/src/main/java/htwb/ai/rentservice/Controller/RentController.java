package htwb.ai.rentservice.Controller;

import htwb.ai.rentservice.Entity.Bike;
import htwb.ai.rentservice.Entity.Booking;
import htwb.ai.rentservice.Repo.BikeRepository;
import htwb.ai.rentservice.Repo.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

import static htwb.ai.rentservice.Helper.*;

@RestController
@RequestMapping("/rent")
public class RentController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BikeRepository bikeRepository;

    @GetMapping("/{id}")
    public ResponseEntity getBookingById(@PathVariable(value = "id") Integer id){
        System.out.println("GET booking by id: "+ id);
       Booking booking = bookingRepository.findById(id).get();
        System.out.println("Booking: "+booking);
        if (booking != null){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(booking.toString());
        } else {
            return (ResponseEntity) ResponseEntity.notFound();
        }
    }


    @GetMapping("/bike/{id}")
    public ResponseEntity getBookingById(@PathVariable(value = "id") String bikeId){
        System.out.println("GET booking by bike: "+ bikeId);
        Booking booking = bookingRepository.findBookingByBikeId(bikeId);
        if (booking != null){
            Bike bike = bikeRepository.findById(booking.getBikeId()).get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(booking+"\n"+bike);
        } else {
            return (ResponseEntity) ResponseEntity.notFound();
        }
    }

    @PostMapping("/create")
    public ResponseEntity createBooking(@RequestParam String bikeId,
                                                @RequestParam String customId){
        System.out.println("Post rent-creat booking: bike:"+bikeId+",custom:"+customId);
        if (bikeId.isEmpty() || customId.isEmpty()){
            return ResponseEntity.badRequest().body("Failure. Empty payload");
        }
        if(!bikeRepository.existsById(bikeId) ){
            return ResponseEntity.badRequest().body("Bike id is wrong");
        }
        Bike bike = bikeRepository.findById(bikeId).get();
        if (!bike.getStatus().equals(BIKE_STATUS_AVAILABLE)){
            return ResponseEntity.badRequest().body("This bike is not for rent right now");
        }

        if (bookingRepository.existsBookingByCustomId(customId)){
            Booking oldBooking = bookingRepository.findBookingByCustomId(customId);
            if (!oldBooking.getStatus().equals(BKG_STATUS_COMPLETED)){
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
            Booking booking = new Booking(customId,bikeId,BKG_STATUS_RESERVED);
            Booking newBooking = bookingRepository.save(booking);
            return ResponseEntity.ok().contentType( MediaType.APPLICATION_JSON).body(newBooking);
        } else {
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
                                     @RequestParam String startTime){
        System.out.println("PUT SERVICE:Start the route: "+ bookingId+",with bike: "+bikeId);
        if(startTime.isEmpty() || bikeId.isEmpty() || bookingId==null
                || !bookingRepository.existsById(bookingId) || !bikeRepository.existsById(bikeId)){
            return  ResponseEntity.notFound().build();
        }
        Booking booking =bookingRepository.findById(bookingId).get();
        if (!booking.getBikeId().equals(bikeId)){
            return ResponseEntity.badRequest().body("Wrong bike id");
        }

        booking.setStatus(BKG_STATUS_RUNNING);
        booking.setStartTime(startTime);
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



}
