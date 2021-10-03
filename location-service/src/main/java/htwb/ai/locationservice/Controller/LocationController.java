package htwb.ai.locationservice.Controller;

import htwb.ai.locationservice.Entity.Location;
import htwb.ai.locationservice.Repo.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/locate")
public class LocationController {

    @Autowired
    private LocationRepository repository;

    @PostMapping("")
    public ResponseEntity insertLocation(@RequestHeader String currentId,
                                         @RequestBody Location payloadLocation) {
        if (!payloadLocation.isAcceptable()) {
            return ResponseEntity.badRequest().body("Wrong format");
        }
        if (!currentId.equals(payloadLocation.getBikeId()))
            return ResponseEntity.badRequest().body("Bike Id mismatch");

        Location newLocation = repository.save(payloadLocation);

        if(newLocation != null){
            if (!newLocation.getBikeId().equals("Tester"))
                updateCurrentBikeLocation(newLocation.getBikeId(),newLocation.getLatitude(),newLocation.getLongtitude());
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(newLocation);
            } else {
                return ResponseEntity.badRequest().body("Failure");
            }
    }

    public void updateCurrentBikeLocation(String bikeId, String latitude, String longtitude){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8300/rent/updatebikelocation?" +
                "bikeId="+bikeId+"&"+
                "latitude="+latitude+"&"+
                "longtitude="+longtitude;
        restTemplate.put(url,null);
    }


}
