package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Chat;
import edu.ftn.iss.eventplanner.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c WHERE c.sender.id IN (:u1, :u2) AND c.recipient.id IN (:u1, :u2)")
    Optional<Chat> findByParticipant(int u1, int u2);

    @Query("SELECT c FROM Chat c WHERE :u1 IN (c.sender.id, c.recipient.id)")
    List<Chat> findByParticipant(int u1);
}
