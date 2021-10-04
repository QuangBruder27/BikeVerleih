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

    // Request 1
    @GetMapping("/{customerId}")
    public ResponseEntity getBonusByCustomerId(@RequestHeader String currentId,
                                               @PathVariable(value = "customerId") String customerId){
        System.out.println("GET bonus score by customerId: "+customerId);
        System.out.println("CurrentId: "+currentId);

        if (!currentId.equals(customerId)){
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
    // Request 2
    @PutMapping("/plus/{customerId}/{payloadDistance}")
    public ResponseEntity addBonusScoreByRent(@HeaderParam("currentId") String currentId,
                                           @PathVariable String customerId,
                                           @PathVariable String payloadDistance){
        System.out.println("Put bonus score by Rent");
        System.out.println("Distance = "+payloadDistance);
        System.out.println("CurrentId: "+currentId);
        System.out.println("CustomerId: "+customerId);

        if (null != currentId)  return ResponseEntity.status(Response.SC_METHOD_NOT_ALLOWED).build();

        if (customerId.isEmpty() ||!repository.existsById(customerId)  ||  payloadDistance == null){
            System.out.println("error");
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
            return ResponseEntity.badRequest().body("No point");
        }
    }

    // Request 3
    @PostMapping("/create")
    public ResponseEntity createBonusAccount(@HeaderParam("currentId") String currentId,
                                             @RequestParam String customerId){
        System.out.println("Bonus Service: Create Bonus Account");
        System.out.println("CurrentId: "+currentId);
        System.out.println("CustomerId: "+customerId);
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

    // Request 4
    @PutMapping("/add/{customerId}/{point}")
    public ResponseEntity addBonusScoreByReport(@HeaderParam("currentId") String currentId,
                                           @PathVariable String customerId,
                                           @PathVariable Integer point){
        System.out.println("Put bonus score ");
        System.out.println("customerId: "+customerId);

        if (null != currentId)  return ResponseEntity.status(Response.SC_METHOD_NOT_ALLOWED).build();

        if (customerId.isEmpty() || !repository.existsById(customerId)){
            System.out.println("payload error");
            return ResponseEntity.badRequest().body("Failure");
        }

        if (point>0){
        Bonus bonusAccount =  repository.findByCustomerId(customerId);
        Integer lastScore = bonusAccount.getScore();
        Integer newScore = lastScore + point;
        bonusAccount.setScore(newScore);
        Bonus newBonus = repository.save(bonusAccount);
        return ResponseEntity.ok().contentType( MediaType.APPLICATION_JSON).body(newBonus);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
