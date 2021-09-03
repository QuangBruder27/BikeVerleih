package htwb.ai.locationservice.Controller;

import htwb.ai.locationservice.Entity.Location;
import htwb.ai.locationservice.Repo.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locate")
public class LocationController {

    @Autowired
    private LocationRepository repository;

    @PostMapping("")
    public ResponseEntity insertPosition(@RequestParam String bikeId,
                                          @RequestParam String latitude,
                                          @RequestParam String longtitude,
                                          @RequestParam String timecreated) {
        if (bikeId.isEmpty() || latitude.isEmpty() || longtitude.isEmpty() || timecreated.isEmpty()) {
            return ResponseEntity.badRequest().body("Wrong format");
        }

        Location location = new Location();
        location.setBikeId(bikeId);
        location.setLatitude(latitude);
        location.setLongtitude(longtitude);
        location.setTimeCreated(timecreated);

        Location newLocation = repository.save(location);

        if(newLocation != null){
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(newLocation);
            } else {
                return ResponseEntity.badRequest().body("Failure");
            }
    }


}
