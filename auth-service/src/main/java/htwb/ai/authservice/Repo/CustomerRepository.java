package htwb.ai.authservice.Repo;


import htwb.ai.authservice.Entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer,String> {
    boolean existsCustomByEmail(String email);
    Customer findCustomByEmail(String email);
}
