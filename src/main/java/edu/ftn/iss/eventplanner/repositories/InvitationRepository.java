package edu.ftn.iss.eventplanner.repositories;
import edu.ftn.iss.eventplanner.entities.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Integer>{
    List<Invitation> findByEventId(Integer eventId);
    boolean existsByGuestEmailAndEventId(String guestEmail, Integer eventId);
    Optional<Invitation> findByGuestEmailAndEventId(String guestEmail, Integer eventId);

}