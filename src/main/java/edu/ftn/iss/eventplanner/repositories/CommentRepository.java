package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Comment;
import edu.ftn.iss.eventplanner.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByStatus(Status status);

}
