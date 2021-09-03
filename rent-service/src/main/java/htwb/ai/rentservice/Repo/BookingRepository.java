package htwb.ai.rentservice.Repo;

import htwb.ai.rentservice.Entity.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Integer> {
    Booking findBookingByBikeId(String bikeId);
    Booking findBookingByCustomId(String customId);
    boolean existsBookingByCustomId(String customId);
}
