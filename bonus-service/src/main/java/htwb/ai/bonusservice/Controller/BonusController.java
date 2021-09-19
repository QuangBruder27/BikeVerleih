package htwb.ai.bonusservice.Controller;


import htwb.ai.bonusservice.Entity.Bonus;
import htwb.ai.bonusservice.Repo.BonusRepository;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.HeaderParam;

@RestController
@RequestMapping("/bonus")
public class BonusController {

    @Autowired
    private BonusRepository repository;

    @GetMapping("/{customerId}")
    public ResponseEntity getBonusByCustomerId(@RequestHeader String currentId,
                                               @PathVariable(value = "customerId") String customerId){
        System.out.println("GET bonus score by customerId: "+customerId);
        if (!currentId.equals(customerId)){
            System.out.println("CurrentId: "+currentId);
            System.out.println("Customer Id mismatch");
            return ResponseEntity.badRequest().body("Customer Id mismatch");
        }

        Bonus  bonus = repository.findByCustomerId(customerId);
        System.out.println("Bonus: "+ bonus);
        if (!bonus.equals(null)){
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(String.valueOf(bonus.getScore()));
        } else {
            return  ResponseEntity.notFound().build();
        }
    }

    @PutMapping("")
    public ResponseEntity updateBonusScore(@HeaderParam("currentId") String currentId,@RequestParam String customerId,
                                           @RequestParam String payloadDistance){
        System.out.println("Put bonus ");
        System.out.println("Distance = "+payloadDistance);

        if (null != currentId)  return ResponseEntity.status(Response.SC_METHOD_NOT_ALLOWED).build();

        if (customerId.isEmpty() || !repository.existsById(customerId)  ||  payloadDistance == null){
            System.out.println("payload error");
            return ResponseEntity.badRequest().body("Failure");
        }
        Integer distance = Integer.valueOf(payloadDistance);
        if (distance>5){
            Bonus bonus =  repository.findByCustomerId(customerId);
            Integer lastScore = bonus.getScore();
            Integer newScore = lastScore + ((distance-5)*10);
            bonus.setScore(newScore);
            Bonus newBonus = repository.save(bonus);
            return ResponseEntity.ok().contentType( MediaType.APPLICATION_JSON).body(newBonus);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("")
    public ResponseEntity createBonusAccount(@HeaderParam("currentId") String currentId,@RequestParam String customerId){
        if (null != currentId)
            return ResponseEntity.status(Response.SC_METHOD_NOT_ALLOWED).build();

        if (customerId.isEmpty() || repository.existsById(customerId)){
            System.out.println("payload error");
            return ResponseEntity.badRequest().body("Failure");
        } else {
            Bonus bonus = new Bonus();
            bonus.setCustomerId(customerId);
            bonus.setScore(0);
            Bonus newBonus = repository.save(bonus);
            return ResponseEntity.ok().contentType( MediaType.APPLICATION_JSON).body(newBonus);
        }
    }


}
