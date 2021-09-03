package htwb.ai.bonusservice.Repo;

import htwb.ai.bonusservice.Entity.Bonus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusRepository extends CrudRepository<Bonus,String> {
    Bonus findByCustomId(String customId);
}