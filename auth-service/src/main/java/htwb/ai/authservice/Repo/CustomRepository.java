package htwb.ai.authservice.Repo;


import htwb.ai.authservice.Entity.Custom;
import org.springframework.data.repository.CrudRepository;

public interface CustomRepository extends CrudRepository<Custom,Integer> {
    boolean existsCustomByEmail(String email);
    Custom findCustomByEmail(String email);
}
