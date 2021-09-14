package htwb.ai.bonusservice.Controller;


import htwb.ai.bonusservice.Entity.Bonus;
import htwb.ai.bonusservice.Repo.BonusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bonus")
public class BonusController {

    @Autowired
    private BonusRepository repository;

    @GetMapping("/{id}")
    public ResponseEntity getBonusByCustomerId(@PathVariable(value = "id") String customerId){
        System.out.println("GET bonus score by customerId: "+customerId);
        Bonus  bonus = repository.findByCustomerId(customerId);
        System.out.println("Bonus: "+ bonus);
        if (!bonus.equals(null)){
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(String.valueOf(bonus.getScore()));
        } else {
            return (ResponseEntity) ResponseEntity.notFound();
        }
    }

    @PutMapping("")
    public ResponseEntity updateBonus(@RequestParam String customerId,
                                      @RequestParam String payloadDistance){
        System.out.println(" Put bonus ");
        System.out.println("Distance = "+payloadDistance);
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
    public ResponseEntity createBonusAccount(@RequestParam String customerId){
        System.out.println(" Post bonus ");
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
