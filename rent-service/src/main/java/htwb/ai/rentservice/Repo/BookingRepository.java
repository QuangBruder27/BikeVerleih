package htwb.ai.rentservice.Repo;

import htwb.ai.rentservice.Entity.Booking;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, Integer> {
    List<Booking> findBookingByBikeId(String bikeId);
    List<Booking> findBookingByCustomerId(String customerId);
    boolean existsBookingByCustomerId(String customerId);
}
