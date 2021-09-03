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
    public ResponseEntity getBonusByCustomId(@PathVariable(value = "id") String customId){
        System.out.println("GET bonus by customId: "+customId);
        Bonus  bonus = repository.findByCustomId(customId);
        System.out.println("Bonus: "+ bonus);
        if (!bonus.equals(null)){
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(bonus.toString());
        } else {
            return (ResponseEntity) ResponseEntity.notFound();
        }
    }

    @PutMapping("")
    public ResponseEntity updateBonus(@RequestParam String customId,
                                      @RequestParam String payloadDistance){
        System.out.println(" Put bonus ");
        System.out.println("Distance = "+payloadDistance);
        if (customId.isEmpty() || !repository.existsById(customId)  ||  payloadDistance == null){
            System.out.println("payload error");
            return ResponseEntity.badRequest().body("Failure");
        }
        Double distance = Double.valueOf(payloadDistance);
        if (distance>5.0){
            Bonus bonus =  repository.findByCustomId(customId);
            double lastScore = bonus.getScore();
            double newScore = lastScore + (double)Math.round((distance-5.0)*10);
            bonus.setScore(newScore);
            Bonus newBonus = repository.save(bonus);
            return ResponseEntity.ok().contentType( MediaType.APPLICATION_JSON).body(newBonus);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("")
    public ResponseEntity insertBonus(@RequestParam String customId){
        System.out.println(" Post bonus ");
        if (customId.isEmpty() || repository.existsById(customId)){
            System.out.println("payload error");
            return ResponseEntity.badRequest().body("Failure");
        } else {
            Bonus bonus = new Bonus();
            bonus.setCustomId(customId);
            bonus.setScore(0.0);
            Bonus newBonus = repository.save(bonus);
            return ResponseEntity.ok().contentType( MediaType.APPLICATION_JSON).body(newBonus);
        }
    }


}
